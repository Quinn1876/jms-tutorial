package com.cli;

import org.junit.Assert;
import org.junit.Test;

public class ArgumentParserTest {
	@Test
	public void test_givenMultiplePositionalArguments_expectToGetValues_whenValuesAreSupplied() {
		ArgumentParser parser = ArgumentParser.builder().addPositionalArgument("arg0").addPositionalArgument("arg1").build();
		try {
			var args = parser.parseArgs(new String[] {"Hello", "World"});
			Assert.assertEquals("Hello", args.getStringArg("arg0"));
			Assert.assertEquals("World", args.getStringArg("arg1"));
		} catch (ArgumentParserException e) {
			e.printStackTrace();
			Assert.fail("Expect not to throw");
		}
	}
	
	@Test
	public void test_givenMultiplePositionalArguments_expectThrow_whenMissingArguments() {
		ArgumentParser parser = ArgumentParser.builder().addPositionalArgument("arg0").addPositionalArgument("arg1").build();
		try {
			parser.parseArgs(new String[] {"Hello"});
			Assert.fail("Expect to throw");
		} catch (ArgumentParserException e) {}
	}
	
	@Test
	public void test_givenMultipleQueries_expectParserToParseAllOfThem() {
		ArgumentParser parser = ArgumentParser.builder().addPositionalArgument("arg0").addPositionalArgument("arg1").build();
		try {
			parser.parseArgs(new String[] {"Hello", "World"});
			parser.parseArgs(new String[] {"Hello", "Again"});
		} catch (ArgumentParserException e) {
			e.printStackTrace();
			Assert.fail("Expect not to throw");
		}
	}
	
	@Test
	public void test_givenMultipleFlags_expectAllToBeParsed() {
		ArgumentParser parser = ArgumentParser.builder().addFlag("-t").addFlag("-e").addFlag("-s").build();
		
		try {
			var args = parser.parseArgs(new String[] {"-test"});
			Assert.assertTrue(args.getFlag("t"));
			Assert.assertTrue(args.getFlag("e"));
			Assert.assertTrue(args.getFlag("s"));
			Assert.assertTrue(args.getFlag("t"));
		} catch (ArgumentParserException e) {
			e.printStackTrace();
			Assert.fail("Expect not to throw");
		}
	}
	
	@Test
	public void test_givenMutlipleArguments_expectAllToBeParsed() {
		ArgumentParser parser = ArgumentParser.builder()
											  .addFlag("-t")
											  .addFlag("--double")
											  .addPositionalArgument("fileName")
											  .addStringArgument("--directory")
											  .build();
		try {
			var args = parser.parseArgs("temp_file -t --directory Baz --double".split(" "));
			Assert.assertTrue(args.getFlag("t"));
			Assert.assertEquals("temp_file", args.getStringArg("fileName"));
			Assert.assertEquals("Baz", args.getStringArg("directory"));
			Assert.assertTrue(args.getFlag("double"));
		} catch (ArgumentParserException e) {
			e.printStackTrace();
			Assert.fail("Expect not to throw");
		}
	}
	
	@Test
	public void test_givenMutlipleArguments_expectFlagsToBeFalse_whenMissing() {
		ArgumentParser parser = ArgumentParser.builder()
											  .addFlag("-t")
											  .addFlag("--double")
											  .addPositionalArgument("fileName")
											  .addStringArgument("--directory")
											  .build();
		try {
			var args = parser.parseArgs("temp_file -t --directory Baz".split(" "));
			Assert.assertTrue(args.getFlag("t"));
			Assert.assertEquals("temp_file", args.getStringArg("fileName"));
			Assert.assertEquals("Baz", args.getStringArg("directory"));
			Assert.assertFalse(args.getFlag("double"));
		} catch (ArgumentParserException e) {
			e.printStackTrace();
			Assert.fail("Expect not to throw");
		}
	}
}
