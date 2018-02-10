package org.unclesniper.arceye.termio;

import org.unclesniper.arceye.utils.SequenceMap;

public class SequenceMapInputConverter implements InputConverter {

	private static class NodeStage implements Stage {

		private final SequenceMap.Node<Byte, KeySym> node;

		NodeStage(SequenceMap.Node<Byte, KeySym> node) {
			this.node = node;
		}

		public KeySym getCurrentSymbol() {
			return node.getValue();
		}

		public boolean isInputLeaf() {
			return node.isLeaf();
		}

		public Stage getNextStage(byte inputByte) {
			SequenceMap.Node<Byte, KeySym> next = node.getChild(inputByte);
			return next == null ? null : new NodeStage(next);
		}

	}

	private final SequenceMap<Byte, KeySym> map = new SequenceMap<Byte, KeySym>();

	public SequenceMap<Byte, KeySym> getMap() {
		return map;
	}

	public Stage newSequence() {
		return new NodeStage(map.getRoot());
	}

}
