//Zertop™
//www.zertop.com
package Reporting;

import Control.Engine;

import java.io.*;
import java.util.Date;

public class GenReport implements Runnable {
    //PERM VARIABLES
    static String pingResults;
    static String determinedIP;
    static String intelReport = "";
    static MinMaxAve testIP;
//PERM VARIABLES

    public void run() {
        //Determine IP to ping
        determinedIP = Tools.determineIP();
        GUI.Interface.setDeterminingIPToPingComplete();

        if (!determinedIP.equals("")) {
            //Generate ping results
            getPingResults();
            GUI.Interface.setPingingTelkomEquipmentComplete();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            } //Sleep Execution for 1s


            //Generate Report
            genReport();
            GUI.Interface.setGeneratingReportComplete();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            } //Sleep Execution for 1s

            //Finish Up
            GUI.Interface.setFinishedComplete();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            } //Sleep Execution for 1s

            //Report Back to Engine
            Control.Engine.goToResults();
        }
    }

    public static void getPingResults() //Generate Ping Results
    {
        pingResults = Tools.pingIP(determinedIP, 3);
    }

    public static void genReport() //Generate report files
    {

        try {
            //INITIALISE WRITERS
            File tempPlain = File.createTempFile("zertoplinetool", ".txt");
            PrintWriter writerPlain = new PrintWriter(tempPlain);

            File tempFormatted = File.createTempFile("zertoplinetool", ".txt");
            PrintWriter writerFormatted = new PrintWriter(tempFormatted);

            //GENERATE MIN MAX AVE VARIABLES
            testIP = new MinMaxAve(pingResults);

            //GENERATE TEXT FILE HEADERS
            writerPlain.println("Zertop's \"Is it my Line\" Results" + Reporting.Tools.getLineBreak() + "Date/Time: " + new Date());
            writerPlain.println("" + Reporting.Tools.getLineBreak() + "Basic Report:");
            writerPlain.println("");

            writerFormatted.println("[B]Zertop's \"Is it my Line\" Results" + Reporting.Tools.getLineBreak() + "Date/Time: " + new Date() + "[/B]");
            writerFormatted.println("[I][B]" + Reporting.Tools.getLineBreak() + "Basic Report:[/B][/I]");
            writerFormatted.println("");

            //GENERATE INTELLIGENT REPORT HEADERS
            intelReport = intelReport + ("Your packet loss was " + testIP.getPacketLoss() + "%.");
            intelReport = intelReport + Reporting.Tools.getLineBreak() + ("Your average ping was " + testIP.getAvePing() + "ms.");
            intelReport = intelReport + Reporting.Tools.getLineBreak() + ("Your maximum ping was " + testIP.getMaxPing() + "ms.");
            intelReport = intelReport + Reporting.Tools.getLineBreak() + ("");

            //PACKET LOSS REPORT
            if (testIP.getPacketLoss() > 20) {
                intelReport = intelReport + Reporting.Tools.getLineBreak() + ("You have serious packet loss. Please post these results to the forum for advice.");
            } else if (testIP.getPacketLoss() > 1) {
                intelReport = intelReport + Reporting.Tools.getLineBreak() + ("You seem to have some packet loss. This indicates an issue on the line.");
            }

            //AVERAGE PING REPORT
            if (testIP.getPacketLoss() < 20) {
                if (testIP.getAvePing() > 80) {
                    intelReport = intelReport + Reporting.Tools.getLineBreak() + ("Looking at your average ping, there is definitely something wrong with your line! Please post the results (at the end of the program) into the forum for advice!");
                } else if (testIP.getAvePing() > 30) {
                    intelReport = intelReport + Reporting.Tools.getLineBreak() + ("Looking at your average ping, your line seems to be a bit dodgy. This may account for any slow speeds you may be experiencing. However, it could just be related to a high-latency home network (eg... Wi-Fi). If you feel the need, please post the results (at the end of the program) into the forum for advice!");
                } else {
                    intelReport = intelReport + Reporting.Tools.getLineBreak() + ("Looking at your average ping, your line seems to be running perfectly.");
                }
            }

            //MAXIMUM PING REPORT
            if (testIP.getPacketLoss() < 20) {
                if (testIP.getAvePing() < 30) {
                    if (testIP.getMaxPing() > 100) {
                        intelReport = intelReport + Reporting.Tools.getLineBreak() + ("Looking at your maximum ping, however, it seems as though there could be a serious intermittent fault on the line. This could be a cause of an issue. Please post the results (at the end of the program) into the forum for advice.");
                    } else if (testIP.getMaxPing() > 30) {
                        intelReport = intelReport + Reporting.Tools.getLineBreak() + ("Looking at your maximum ping, however, it seems as though there could be a slight intermittant issue on the line. If required, please post the results to a forum post.");
                    } else {
                        intelReport = intelReport + Reporting.Tools.getLineBreak() + ("It seems that your maximum ping is also good. If there are any issues, they most probably lie with your ISP");
                    }
                }
            }

            writerPlain.println(intelReport);
            writerFormatted.println(intelReport);

            //ATTACHING DETAILED REPORT
            writerPlain.println("");
            writerPlain.println("Detailed Report:");
            writerPlain.println(pingResults);

            writerFormatted.println("");
            writerFormatted.println("[B][I]Detailed Report:[/B][/I]");
            writerFormatted.println("[CODE]");
            writerFormatted.println(pingResults);
            writerFormatted.println("[/CODE]");

            //SAVE AS TMP AND PARSE TO ENGINE
            writerPlain.close();
            writerFormatted.close();
            Engine.setIntelligentReport(intelReport);
            Engine.setPlainTxtPath(tempPlain.getAbsolutePath());
            Engine.setFormattedTxtPath(tempFormatted.getAbsolutePath());

        } catch (IOException ex) {
        }
    }
//GENERATE REPORT
}
