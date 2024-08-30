package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {

        String path = "src/main/resources";
        String inputMessage = new String();
        String inputXmlSchema = new String();
        try {
            inputMessage = new String(Files.readAllBytes(Paths.get(path+"/mt/103.mt")));
            inputXmlSchema = new String(Files.readAllBytes(Paths.get(path+"/xml/103.xml")));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
        inputXmlSchema = createHeader(inputMessage, inputXmlSchema);
        System.out.println(createBody(inputMessage, inputXmlSchema));
    }

    static private String createHeader(String inputMessage, String inputSchema){
// Extract the substring for bicFiVar1 from inputMessage
        String bicFiVar1Section = inputMessage.substring(inputMessage.indexOf("{1:") + 3, inputMessage.indexOf("}", inputMessage.indexOf("{1:")));
        String bicFiVar1 = bicFiVar1Section.substring(3, 11) + bicFiVar1Section.substring(12, 15);

        // Extract the substring for bicFiVar2 from inputMessage
        String bicFiVar2Section = inputMessage.substring(inputMessage.indexOf("{2:") + 3, inputMessage.indexOf("}", inputMessage.indexOf("{2:")));
        String bicFiVar2 = bicFiVar2Section.substring(4, 12) + bicFiVar2Section.substring(13, 16);

        // Get current system time in the required format
        String systemTimeVar = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());

        inputSchema = inputSchema.replace("bicFiVar1", bicFiVar1)
                .replace("bicFiVar2", bicFiVar2)
                .replace("systemTimeVar", systemTimeVar);

        return inputSchema;
    }

    private static String createBody(String inputMessage, String inputSchema){
        String systemTimeVar = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String msgIdVar = getSubstringBetween(inputMessage, ":20:");
        String identifier53B = getSubstringBetween(inputMessage, ":53B:/");
        String identifier21 = getSubstringBetween(inputMessage, ":21:");
        String identifier52A = getSubstringBetween(inputMessage, ":52A:");
        String identifier57A = getSubstringBetween(inputMessage, ":57A:");
        String identifier58A = getSubstringBetween(inputMessage, ":58A:/");
        String identifier72A = getSubstringBetween(inputMessage, ":72A:");
        String identifier32A = getSubstringBetween(inputMessage, ":32A:");
        String identifierDate = identifier32A.substring(0, 6);
        String formattedDate = parseAndFormatDate(identifierDate);


        inputSchema = inputSchema.replace("msgIdVar", msgIdVar)
                .replace("systemTimeVar", systemTimeVar)
                .replace("identifier53B", identifier53B)
                .replace("identifier21", identifier21)
                .replace("identifier52A", identifier52A)
                .replace("identifier57A", identifier57A)
                .replace("identifier58A", identifier58A)
                .replace("identifier72A", identifier72A)
                .replace("identifierCurrency", identifier32A.substring(6, 9))
                .replace("identifierAmount", identifier32A.substring(9))
                .replace("identifierDate", formattedDate);
        return inputSchema;
    }

    public static String getSubstringBetween(String input, String startToken) {
        int startIndex = input.indexOf(startToken);
        if (startIndex == -1) {
            return ""; // Start token not found
        }
        startIndex += startToken.length(); // Move to the end of the start token
        int endIndex = input.indexOf(":", startIndex);
        if (endIndex == -1) {
            return ""; // End token not found
        }
        return input.substring(startIndex, endIndex).trim();
    }

    public static String parseAndFormatDate(String dateString){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy-MM-dd");
        String formattedDate = "";
        try {
            Date date = inputFormat.parse(dateString);
            formattedDate = outputFormat.format(date);
        }
        catch (ParseException ex){
            System.out.println("Invalid date");
        }
        return formattedDate;
    }
}