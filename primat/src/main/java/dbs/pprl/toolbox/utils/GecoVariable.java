package dbs.pprl.toolbox.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Random;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class GecoVariable {
	
	public static void main(String[] args) throws Exception {	
        Options options = new Options();

        Option outputPathOption = new Option("o", "outputPath", true, "outputPath");
        outputPathOption.setRequired(true);
        options.addOption(outputPathOption);
        Option attributesOption = new Option("a", "attributes", true, "attributes");
        attributesOption.setRequired(true);
        options.addOption(attributesOption);
        Option regionOption = new Option("r", "region", true, "region \\\"G\\\" or \\\"L\\\"");
        options.addOption(regionOption);
        Option indexSizeOption = new Option("i", "indexSize", true, "indexSize");
        indexSizeOption.setRequired(true);
        options.addOption(indexSizeOption);
        Option querySizeOption = new Option("q", "querySize", true, "querySize");
        querySizeOption.setRequired(true);
        options.addOption(querySizeOption);
        Option queryOverlapOption = new Option("qo", "queryOverlap", true, "queryOverlap");
        queryOverlapOption.setRequired(true);
        options.addOption(queryOverlapOption);
        Option familyRateOption = new Option("fr", "familyRate", true, "familyRate");
        familyRateOption.setRequired(true);
        options.addOption(familyRateOption);
        Option moveRateOption = new Option("mr", "moveRate", true, "moveRate");
        moveRateOption.setRequired(true);
        options.addOption(moveRateOption);
        Option errorRatesOption = new Option("er", "errorRates", true, "errorRates (\",\"-separated)");
        errorRatesOption.setRequired(true);
        options.addOption(errorRatesOption);
        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

		String outputPath = cmd.getOptionValue("outputPath");
		String region;
		if(cmd.hasOption("region")) {
			region = cmd.getOptionValue("region");
		} else {
			region = "G";
		}
		int indexSize = Integer.parseInt(cmd.getOptionValue("indexSize"));
		int querySize = Integer.parseInt(cmd.getOptionValue("querySize"));
		int queryOverlap = Integer.parseInt(cmd.getOptionValue("queryOverlap"))*querySize/100;
		int queryOverlapRemaining = queryOverlap;
		int familyRate = Integer.parseInt(cmd.getOptionValue("familyRate"));
		int moveRate = Integer.parseInt(cmd.getOptionValue("moveRate"))*queryOverlap/100;
		String[] errorRateSizesString = cmd.getOptionValue("errorRates").split(",");
		int numberOfErrorRates = errorRateSizesString.length;
		int[] errorRateSizes = new int[numberOfErrorRates];
		errorRateSizes[0] = Integer.parseInt(errorRateSizesString[0])*queryOverlap/100;
		String gecoFilePath;
		long id = 0;
		for(int i = 1; i < numberOfErrorRates; i++) {
			System.out.println(errorRateSizesString[i]);
			errorRateSizes[i] = Integer.parseInt(errorRateSizesString[i])*queryOverlap/100;
			System.out.println(errorRateSizes[i]);
			if(errorRateSizes[i] > 0) {
				queryOverlapRemaining -= errorRateSizes[i];
				System.out.println("creating Geco File with errors " + i + " of " + (numberOfErrorRates-1));
				gecoFilePath = executeGeco(i, region, 1, i, errorRateSizes[i]+1, errorRateSizes[i]+1, outputPath);
				System.out.println("copying from Geco File with errors " + i + " of " + (numberOfErrorRates-1));
				id = writeData(id, outputPath, gecoFilePath, errorRateSizes[i], familyRate, true, true, true, false);
			}
		}
		if(moveRate > 0) {
			queryOverlapRemaining -= moveRate;
			System.out.println("creating Geco File for moved persons");
			gecoFilePath = executeGeco(43, region, 1, 1, moveRate+1, 1, outputPath);
			System.out.println("copying from Geco File for moved persons");
			id = writeData(id, outputPath, gecoFilePath, moveRate, familyRate, true, true, false, true);
		}
		if(queryOverlapRemaining > 0) {
			System.out.println("creating Geco File for no errors");
			gecoFilePath = executeGeco(42, region, 1, 1, queryOverlapRemaining+1, 2, outputPath);
			System.out.println("copying from Geco File for no errors");
			id = writeData(id, outputPath, gecoFilePath, queryOverlapRemaining, familyRate, true, true, false, false);
		}
		if((querySize - queryOverlap) > 0) {
			System.out.println("creating Geco File for no overlap");
			gecoFilePath = executeGeco(41, region, 1, 1, querySize - queryOverlap+1, 3, outputPath);
			System.out.println("copying from Geco File for no overlap");
			id = writeData(id, outputPath, gecoFilePath, querySize - queryOverlap, familyRate, false, true, false, false);
		}
		if((indexSize - queryOverlap) > 0) {
			System.out.println("creating Geco File for remaining index");
			gecoFilePath = executeGeco(40, region, 1, 1, indexSize - queryOverlap+1, 4, outputPath);
			System.out.println("copying from Geco File for remaining index");
			id = writeData(id, outputPath, gecoFilePath, indexSize - queryOverlap, familyRate, true, false, false, false);
		}
		
    }
	
	private static long writeData(long startId, String outputPath, String path, int numberOfRecords, int familyRate, boolean withIndex, boolean withQuery, boolean useExistingQuery, boolean moved) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        FileWriter fw = new FileWriter(outputPath + "data.csv", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        String line = br.readLine();
        Random rand = new Random(familyRate);
        familyRate = numberOfRecords*familyRate/100;
        String familyName = "";
        String familyCity = "";
        String newFamilyCity = "";
        String newCity;
        String lastCity;
        long id = startId;
        int familySize = 0;
        line = br.readLine();
        String[] fields  = line.split(",");
        String[] lastFields;
        while (((line = br.readLine()) != null) && ((id-startId) < numberOfRecords)) {
        	lastFields = fields;
        	fields = line.split(",");
        	String[] recordId = fields[0].split("-");
        	if(familySize == 0 && familyRate > 0) {
        		familySize = rand.nextInt(4) + 2;
        		familyRate -= familySize;
        		familyName = fields[2];
        		familyCity = fields[3];
        		newFamilyCity = lastFields[3];
        	}
			if(familySize > 0) {
				if(!useExistingQuery) { 	//TODO for existing error files
					line = line.replace(fields[2],familyName);
	        		line = line.replace(fields[3],familyCity);
				}
        	}
        	if(withQuery && withIndex) {
        		if(useExistingQuery) {		// use Geco errors in query
        			if(recordId[2].equals("dup")) {
		        		line = line.replace("-"+recordId[1]+"-","-"+id+"-");
		        		pw.println(line);
	        			line = br.readLine();
	        			line = line.replace("-"+recordId[1]+"-","-"+id+"-");
		            	pw.println(line);
		            	id++;
        			}
        		} else if(moved) {			// move persons from index to query
        			if(recordId[2].equals("org")) {
            			if(familySize > 0) {
            				newCity = newFamilyCity;
            				lastCity = familyCity;
                    	} else {
            				newCity = lastFields[3];
            				lastCity = fields[3];
                    	}
        				line = line.replace("-"+recordId[1]+"-","-"+id+"-");
		        		pw.println(line);
		        		line = line.replace("org","dup");
		        		line = line.replace(lastCity,newCity);
		        		pw.println(line);
		        		id++;
        			}
        		} else {					// duplicate index to query
        			if(recordId[2].equals("org")) {
        				line = line.replace("-"+recordId[1]+"-","-"+id+"-");
		        		pw.println(line);
		        		line = line.replace("org","dup");
		        		pw.println(line);
		        		id++;
        			}
        		}
        	} else {						// index or query with no overlap
        		if(recordId[2].equals("org")) {
        			line = line.replace("-"+recordId[1]+"-","-"+id+"-");
        			if(withQuery) {
        				line = line.replace("org","dup");
        			}
        			pw.println(line);
        			id++;
        		}
        	}
			if(familySize > 0) {
				familySize--;
			}
        }
        System.out.println("- running id: " + id);
        pw.close();
        bw.close();
        fw.close();
        br.close();
        return id;
	}
	
	private static String executeGeco(int random, String region, int errorsAttr, int errorsRec, int indexSize, int querySize, String outputPath) throws IOException, InterruptedException {
		Process p;
		
		System.out.println("Run python: " + outputPath);
		
		p = Runtime.getRuntime()
				.exec("python generate-data-new.py " + 
		random + " " + region + " " + errorsAttr + " " + errorsRec + " "
						+ indexSize + " " + querySize + " " + outputPath, 
						null, new File("/home/mfranke/Files/marcelsstuff/workspace/Geco/"));	
		         
		
		System.out.println("- python generate-data-new.py " + random + " " + region + " " + errorsAttr + " " + errorsRec + " " + indexSize + " " + querySize + " " + outputPath);
		
		
		p.waitFor();

		/*
		//Output from Process:
		String line;
		BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		while((line = error.readLine()) != null){
		    System.out.println(line);
		}
		error.close();
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while((line=input.readLine()) != null){
		    System.out.println(line);
		}
		input.close();
		
		OutputStream outputStream = p.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		printStream.println();
		printStream.flush();
		printStream.close();
		*/
				
		return outputPath + region + "_" + errorsAttr + "_" + errorsRec + "_" + indexSize + "_" + querySize + ".csv";
	}
}