package com.cli;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashMap;

public class ArgumentParser {
	public static class Builder {
		private HashSet<String> optionalStringArgs = new HashSet<>();
		private HashSet<String> optionalBooleanArgs = new HashSet<>();
		private Queue<String> positionalArgs = new LinkedList<>();
		
		public Builder addStringArgument(String argumentName) {
			optionalStringArgs.add(argumentName);
			return this;
		}
		
		public Builder addFlag(String argumentName) {
			optionalBooleanArgs.add(argumentName);
			return this;
		}
		
		public Builder addPositionalArgument(String argumentName) {
			positionalArgs.add(argumentName);
			return this;
		}
		
		public ArgumentParser build() {
			return new ArgumentParser(this);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	private ArgumentParser(Builder builder) {
		optionalStringArgs = builder.optionalStringArgs;
		optionalBooleanArgs = builder.optionalBooleanArgs;
		positionalArgs = builder.positionalArgs;
	}
	
	public ArgumentParser(ArgumentParser other) {
		optionalStringArgs = new HashSet<>(other.optionalStringArgs);
		optionalBooleanArgs = new HashSet<>(other.optionalBooleanArgs);
		positionalArgs = new LinkedList<String>(other.positionalArgs);
	}
	
	private HashSet<String> optionalStringArgs;
	private HashSet<String> optionalBooleanArgs;
	private Queue<String> positionalArgs;
	
	private boolean isOptional(String arg) {
		return arg.startsWith("-");
	}
	
	public Args parseArgs(String[] argv) throws ArgumentParserException {
		ArgumentParser parser = new ArgumentParser(this);
		Args args = new Args();
		for (int i = 0; i < argv.length; i++) {
			String arg = argv[i];
			if (isOptional(arg)) {
				if (parser.positionalArgs.size() > 0) {
					throw new ArgumentParserException("ArgumentParser does not support optional arguments prior to possitional ones");
				}
				if (parser.optionalStringArgs.contains(arg)) {
					if (i+1 == argv.length) {
						throw new ArgumentParserException("Expected a value following '" + arg + "'");
					}
					String argVal = argv[++i];
					if (isOptional(argVal)) {
						throw new ArgumentParserException("Excepted a value to follow '" + arg +"' but instead received another argument '" + argVal + "'");
					}
					args.addStringArg(arg.replace("-", ""), argVal);
				}
				else if (arg.startsWith("--") && parser.optionalBooleanArgs.contains(arg)) {
					args.addFlag(arg.replace("-", ""));
				} 
				else {
					for (char c : arg.replace("-","").toCharArray()) {
						if (parser.optionalBooleanArgs.contains("-"+c)) {
							args.addFlag(String.valueOf(c));
						} else {
							throw new ArgumentParserException("Unexpected argument: '" + arg + "'");
						}
					}
				}
			} else {
				String argName = parser.positionalArgs.poll();
				if (argName == null) {
					throw new ArgumentParserException("Unexpected positional argument with value: " + arg);
				}
				args.addStringArg(argName, arg);
			}
		}
		
		if (parser.positionalArgs.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("Missing the following positional arguments: [");
			for (var posArg : parser.positionalArgs) {
				builder.append(posArg);
				builder.append(", ");
			}
			builder.append("]");
			throw new ArgumentParserException(builder.toString());
		}
		
		return args;
	}
	
	public static class Args {
		private Args() {}
		
		private void addStringArg(String argName, String argValue) throws ArgumentParserException {
			if (stringArgs.containsKey(argName)) {
				throw new ArgumentParserException("Duplicate arguemnt detected: " + argName);
			}
			stringArgs.put(argName, argValue);
		}
		
		private void addFlag(String argName) {
			boolArgs.add(argName);
		}
		
		public String getStringArg(String argName) {
			return stringArgs.get(argName);
		}
		
		public boolean getFlag(String name) {
			return boolArgs.contains(name);
		}
		
		private HashMap<String, String> stringArgs = new HashMap<>();
		private HashSet<String> boolArgs = new HashSet<>();
	}
}
