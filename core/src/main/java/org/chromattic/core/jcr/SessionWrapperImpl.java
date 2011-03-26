/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.chromattic.core.jcr;

import org.chromattic.common.logging.Logger;
import org.chromattic.spi.jcr.SessionLifeCycle;

import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeManager;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class SessionWrapperImpl implements SessionWrapper {

  /** . */
  private final Logger log = Logger.getLogger(SessionWrapperImpl.class);

  /** . */
  private static final ConcurrentHashMap<Session, SessionWrapperImpl> sessionMapping = new ConcurrentHashMap<Session, SessionWrapperImpl>();

  /** . */
  public final Session session;

  /** . */
  private AbstractLinkManager[] linkMgrs;

  /** . */
  private SessionLifeCycle sessionLifeCycle;

  public SessionWrapperImpl(SessionLifeCycle sessionLifeCycle, Session session) {

    //
    this.sessionLifeCycle = sessionLifeCycle;
    this.session = session;
    this.linkMgrs = new AbstractLinkManager[] {
      new ReferenceLinkManager(session),
      new PathLinkManager(session)
    };

    //
    sessionMapping.put(session, this);
  }

  public NodeType getNodeType(String nodeTypeName) throws RepositoryException {
    NodeTypeManager mgr = session.getWorkspace().getNodeTypeManager();
    return mgr.getNodeType(nodeTypeName);
  }

  public Node addNode(String relPath, NodeDef nodeType) throws RepositoryException {
    return addNode(session.getRootNode(), relPath, nodeType);
  }

  public Node addNode(Node parentNode, String relPath, NodeDef nodeType) throws RepositoryException {
    Node childNode = parentNode.addNode(relPath, nodeType.getPrimaryNodeTypeName());
    for (String mixinName : nodeType.getMixinNames()) {
      childNode.addMixin(mixinName);
    }
    return childNode;
  }

  public Node getNodeByUUID(String uuid) throws RepositoryException {
    return session.getNodeByUUID(uuid);
  }

  public Node getParent(Node childNode) throws RepositoryException {
    return childNode.getParent();
  }

  public Iterator<Node> getChildren(Node parentNode) throws RepositoryException {
    return (Iterator<Node>)parentNode.getNodes();
  }

  public Node getChild(Node parentNode, String name) throws RepositoryException {
    if (parentNode.hasNode(name)) {
      return parentNode.getNode(name);
    } else {
      return null;
    }
  }

  /**
   * Remove a node recursively in order to have one remove event generated for every descendants of the node in order to
   * keep the contexts state corrects. It also remove all existing references to that node.
   *
   * @param node the node to remove
   * @throws RepositoryException any repository exception
   */
  public Iterator<String> remove(Node node) throws RepositoryException {
    LinkedList<String> ids = new LinkedList<String>();

    //
    remove(node, ids);

    // Remove now
    return ids.iterator();
  }

  public void remove(Node node, LinkedList<String> ids) throws RepositoryException {
    for (NodeIterator i = node.getNodes(); i.hasNext();) {
      Node child = i.nextNode();
      remove(child, ids);
    }

    // Cleanup
    for (PropertyIterator i = node.getReferences(); i.hasNext();) {
      Property property = i.nextProperty();
      property.setValue((Node)null);
    }

    // Update reference manager
    for (PropertyIterator i = node.getProperties(); i.hasNext();) {
      Property property = i.nextProperty();
      if (property.getType() == PropertyType.REFERENCE) {
        linkMgrs[LinkType.REFERENCE.index].setReferenced(node, property.getName(), null);
      } else if (property.getType() == PropertyType.PATH) {
        linkMgrs[LinkType.PATH.index].setReferenced(node, property.getName(), null);
      }
    }

    // Remove now
    String id = node.getUUID();
    node.remove();
    ids.add(id);
  }

  public void save() throws RepositoryException {
    sessionLifeCycle.save(session);
    for (AbstractLinkManager mgr : linkMgrs) {
      mgr.clear();
    }
  }

  public Node getReferenced(Node referent, String propertyName, LinkType linkType) throws RepositoryException {
    return linkMgrs[linkType.index].getReferenced(referent, propertyName);
  }

  public Node setReferenced(Node referent, String propertyName, Node referenced, LinkType linkType) throws RepositoryException {
    return linkMgrs[linkType.index].setReferenced(referent, propertyName, referenced);
  }

  public Iterator<Node> getReferents(Node referenced, String propertyName, LinkType linkType) throws RepositoryException {
    return linkMgrs[linkType.index].getReferents(referenced, propertyName);
  }

  @Override
  public int hashCode() {
    return session.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof SessionWrapperImpl) {
      SessionWrapperImpl that = (SessionWrapperImpl)obj;
      return session == that.session;
    }
    return false;
  }

  public Session getSession() {
    return session;
  }

  public void close() {
    for (AbstractLinkManager mgr : linkMgrs) {
      mgr.clear();
    }
    sessionLifeCycle.close(session);
  }
}