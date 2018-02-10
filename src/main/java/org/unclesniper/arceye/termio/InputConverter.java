package org.unclesniper.arceye.termio;

public interface InputConverter {

	public interface Stage {

		KeySym getCurrentSymbol();

		boolean isInputLeaf();

		Stage getNextStage(byte inputByte);

	}

	Stage newSequence();

}
