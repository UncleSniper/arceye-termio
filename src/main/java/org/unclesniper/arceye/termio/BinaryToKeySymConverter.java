package org.unclesniper.arceye.termio;

import java.util.List;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class BinaryToKeySymConverter {

	private static class Chunk {

		private final byte[] bytes;

		private final int offset;

		private final int length;

		Chunk(byte[] bytes, int offset, int length) {
			this.bytes = bytes;
			this.offset = offset;
			this.length = length;
		}

	}

	private final List<InputConverter> inputConverters = new LinkedList<InputConverter>();

	private final List<SingleByteConverter> singleByteConverters = new LinkedList<SingleByteConverter>();

	private InputConverter.Stage[] stages;

	private KeySym lastSymbol;

	private int lastLength;

	private final Deque<Chunk> chunks = new LinkedList<Chunk>();

	private final Deque<KeySym> symbolQueue = new LinkedList<KeySym>();

	private int flushPointer;

	private int currentDepth;

	private int pathCount;

	public Iterable<InputConverter> getInputConverters() {
		return inputConverters;
	}

	public void addInputConverter(InputConverter converter) {
		if(converter != null)
			inputConverters.add(converter);
	}

	public boolean removeInputConverter(InputConverter converter) {
		if(converter == null)
			return false;
		Iterator<InputConverter> it = inputConverters.iterator();
		while(it.hasNext()) {
			if(it.next() == converter) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	public void clearInputConverters() {
		inputConverters.clear();
	}

	public Iterable<SingleByteConverter> getSingleByteConverters() {
		return singleByteConverters;
	}

	public void addSingleByteConverter(SingleByteConverter converter) {
		if(converter != null)
			singleByteConverters.add(converter);
	}

	public boolean removeSingleByteConverter(SingleByteConverter converter) {
		if(converter == null)
			return false;
		Iterator<SingleByteConverter> it = singleByteConverters.iterator();
		while(it.hasNext()) {
			if(it.next() == converter) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	public void clearSingleByteConverters() {
		singleByteConverters.clear();
	}

	public KeySym nextKey() {
		return symbolQueue.isEmpty() ? null : symbolQueue.removeFirst();
	}

	public void feedInput(byte[] buffer, int offset, int length) {
		if(length <= 0)
			return;
		chunks.addLast(new Chunk(buffer, offset, length));
		int end = offset + length;
		boolean pushed = false;
		for(int i = offset; i < end; ++i) {
			if(feedByte(buffer[i])) {
				pushed = true;
				break;
			}
		}
		while(pushed)
			pushed = feedChunkQueue();
	}

	private boolean feedChunkQueue() {
		boolean first = true;
		for(Chunk chunk : chunks) {
			int start = chunk.offset, end = chunk.offset + chunk.length;
			if(first)
				start += flushPointer;
			first = false;
			for(int i = start; i < end; ++i) {
				if(feedByte(chunk.bytes[i]))
					return true;
			}
		}
		return false;
	}

	private boolean feedByte(byte input) {
		KeySym sym;
		if(currentDepth == 0)
			beginSequences();
		advanceSequences(input);
		if(currentDepth == 0)
			processSingles(input);
		++currentDepth;
		if(pathCount == 0) {
			endPaths(input);
			return true;
		}
		else
			return false;
	}

	private void beginSequences() {
		stages = new InputConverter.Stage[inputConverters.size()];
		Iterator<InputConverter> it = inputConverters.iterator();
		pathCount = 0;
		for(int i = 0; i < stages.length; ++i) {
			stages[i] = it.next().newSequence();
			if(stages[i] == null)
				continue;
			++pathCount;
			KeySym sym = stages[i].getCurrentSymbol();
			if(lastSymbol == null && sym != null) {
				lastSymbol = sym;
				lastLength = 0;
			}
			if(stages[i].isInputLeaf()) {
				stages[i] = null;
				--pathCount;
			}
		}
	}

	private void advanceSequences(byte input) {
		KeySym nextSym = null;
		for(int i = 0; i < stages.length; ++i) {
			if(stages[i] == null)
				continue;
			stages[i] = stages[i].getNextStage(input);
			if(stages[i] == null)
				--pathCount;
			else {
				KeySym sym = stages[i].getCurrentSymbol();
				if(nextSym == null && sym != null)
					nextSym = sym;
				if(stages[i].isInputLeaf()) {
					stages[i] = null;
					--pathCount;
				}
			}
		}
		if(nextSym != null) {
			lastSymbol = nextSym;
			lastLength = currentDepth + 1;
		}
	}

	private void processSingles(byte input) {
		for(SingleByteConverter conv : singleByteConverters) {
			KeySym sym = conv.byteToKeySym(input);
			if(lastSymbol == null && sym != null) {
				lastSymbol = sym;
				lastLength = 1;
			}
		}
	}

	private void endPaths(byte input) {
		stages = null;
		if(lastSymbol == null) {
			lastSymbol = new KeySym(KeySym.Type.GENERIC, KeySym.Modifier.NONE, (char)((input + 256) % 256));
			lastLength = 1;
		}
		symbolQueue.addLast(lastSymbol);
		lastSymbol = null;
		while(!chunks.isEmpty() && flushPointer + lastLength >= chunks.getFirst().length) {
			lastLength -= chunks.removeFirst().length - flushPointer;
			flushPointer = 0;
		}
		flushPointer += lastLength;
		lastLength = 0;
		currentDepth = 0;
	}

	public void flushSequences() {
		if(lastSymbol == null)
			return;
		stages = null;
		symbolQueue.addLast(lastSymbol);
		lastSymbol = null;
		while(!chunks.isEmpty() && flushPointer + lastLength >= chunks.getFirst().length) {
			lastLength -= chunks.removeFirst().length - flushPointer;
			flushPointer = 0;
		}
		flushPointer += lastLength;
		lastLength = 0;
		currentDepth = 0;
	}

	public boolean hasPendingKey() {
		return lastSymbol != null;
	}

}
