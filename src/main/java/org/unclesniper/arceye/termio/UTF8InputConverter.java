package org.unclesniper.arceye.termio;

public class UTF8InputConverter implements InputConverter {

	private static class DecodeStage implements Stage {

		enum State {
			NONE,
			SEQ2BYTE0,
			SEQ3BYTE0,
			SEQ3BYTE1,
			DONE;
		}

		private final State state;

		private final int code;

		private DecodeStage(State state, int code) {
			this.state = state;
			this.code = code;
		}

		DecodeStage() {
			this(State.NONE, 0);
		}

		public KeySym getCurrentSymbol() {
			if(state != State.DONE)
				return null;
			return new KeySym(KeySym.Type.GENERIC, KeySym.Modifier.NONE, (char)code);
		}

		public boolean isInputLeaf() {
			return state == State.DONE;
		}

		public Stage getNextStage(byte inputByte) {
			int value = (inputByte + 256) % 256;
			switch(state) {
				case NONE:
					if((value & 0x80) == 0)
						return new DecodeStage(State.DONE, value & 0x7F);
					if((value & 0xE0) == 0xC0)
						return new DecodeStage(State.SEQ2BYTE0, value & 0x1F);
					if((value & 0xF0) == 0xE0)
						return new DecodeStage(State.SEQ3BYTE0, value & 0x0F);
					return null;
				case SEQ3BYTE0:
					if((value & 0xC0) == 0x80)
						return new DecodeStage(State.SEQ3BYTE1, (code << 6) | (value & 0x3F));
					return null;
				case SEQ2BYTE0:
				case SEQ3BYTE1:
					if((value & 0xC0) == 0x80)
						return new DecodeStage(State.DONE, (code << 6) | (value & 0x3F));
					return null;
				case DONE:
					return null;
				default:
					throw new Error("Unrecognized state: " + state.name());
			}
		}

	}

	public static final InputConverter instance = new UTF8InputConverter();

	public Stage newSequence() {
		return new DecodeStage();
	}

}
