package com.aptima.netstorm.algorithms.aptima.bp.network;

/**
 * Basic network relation that stores an id, source, and destination.  Other relationship types
 * should extend this class if they need to store additional algorithm specific information.
 *  
 * @author jroberts
 * 
 */
public class AdjacencyListRelation {
	private static final long serialVersionUID = -7900237807701383677L;
	private int id;
	private int fileId;
	private int fromNode;
	private int toNode;
	private String sFrom;  // Printable fromNode
	private String sTo;  // Printable toNode
	
	public AdjacencyListRelation(int id)
	{
		this.id = id;
	}
	
	public String getFrom() {
		if (sFrom == null) {
			sFrom = "" + fromNode;
		}
		return sFrom;
	}

	public String getTo() {
		if (sTo == null) {
			sTo = "" + toNode;
		}
		return sTo;
	}

	public void setToNode(int toNode) {
		this.toNode = toNode;
		sTo = null;
		getTo(); // string version
	}

	public int getToNode() {
		return toNode;
	}

	public void setFromNode(int fromNode) {
		this.fromNode = fromNode;
		sFrom = null;
		getFrom(); // string version
	}

	public int getFromNode() {
		return fromNode;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFileId() {
		return this.fileId;
	}
	
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
}
