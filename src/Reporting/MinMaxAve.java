//Zertop™
//www.zertop.com
package Reporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinMaxAve {
    //VARIABLES
    private static int maxPing = 0;
    private static int minPing = 0;
    private static int avePing = 0;
    private static int packetLoss = 0;
//VARIABLES

    //GETS
    public int getMaxPing() {
        return maxPing;
    }

    public int getMinPing() {
        return minPing;
    }

    public int getAvePing() {
        return avePing;
    }

    public int getPacketLoss() {
        return packetLoss;
    }
//GETS

    //GENERATE MINMAXAVE
    MinMaxAve(String pingResults) {
//WINDOWS
        if (OSDetection.isWindows()) {
            Pattern packetLossPattern = Pattern.compile("Lost.*\\((\\d*)");
            Matcher packetLossMatcher = packetLossPattern.matcher(pingResults);
            if (packetLossMatcher.find()) {
                packetLoss = Integer.parseInt(packetLossMatcher.group(1));
            }

            Pattern maxPattern = Pattern.compile("Maximum...(\\d*)");
            Matcher maxMatcher = maxPattern.matcher(pingResults);
            if (maxMatcher.find()) {
                maxPing = Integer.parseInt(maxMatcher.group(1));
            }

            Pattern minPattern = Pattern.compile("Minimum...(\\d*)");
            Matcher minMatcher = minPattern.matcher(pingResults);
            if (minMatcher.find()) {
                minPing = Integer.parseInt(minMatcher.group(1));
            }

            Pattern avePattern = Pattern.compile("Average...(\\d*)");
            Matcher aveMatcher = avePattern.matcher(pingResults);
            if (aveMatcher.find()) {
                avePing = Integer.parseInt(aveMatcher.group(1));
            }
        }
//WINDOWS

//LINUX
        if (OSDetection.isUnix()) {
            Pattern pattern = Pattern.compile("rtt\\ min\\/avg\\/max\\/mdev\\ \\=\\ (\\d*).\\d*\\/(\\d*).\\d*\\/(\\d*).\\d*");
            Matcher Matcher = pattern.matcher(pingResults);
            if (Matcher.find()) {
                maxPing = Integer.parseInt(Matcher.group(3));
                minPing = Integer.parseInt(Matcher.group(1));
                avePing = Integer.parseInt(Matcher.group(2));
            }

            Pattern packetLossPattern = Pattern.compile("(\\d*)\\%\\ packet\\ loss");
            Matcher packetLossMatcher = packetLossPattern.matcher(pingResults);
            if (packetLossMatcher.find()) {
                packetLoss = Integer.parseInt(packetLossMatcher.group(1));
            } else {
                packetLoss = -1;
            }
        }
//LINUX  

//OSX
        if (OSDetection.isMac()) {
            Pattern pattern = Pattern.compile("round-trip\\ min\\/avg\\/max\\/stddev\\ \\=\\ (\\d*).\\d*\\/(\\d*).\\d*\\/(\\d*).\\d*");
            Matcher Matcher = pattern.matcher(pingResults);
            if (Matcher.find()) {
                maxPing = Integer.parseInt(Matcher.group(3));
                minPing = Integer.parseInt(Matcher.group(1));
                avePing = Integer.parseInt(Matcher.group(2));
            }

            Pattern packetLossPattern = Pattern.compile("(\\d*)\\%\\ packet\\ loss");
            Matcher packetLossMatcher = packetLossPattern.matcher(pingResults);
            if (packetLossMatcher.find()) {
                packetLoss = Integer.parseInt(packetLossMatcher.group(1));
            } else {
                packetLoss = -1;
            }
        }
//OSX       
    }
//GENERATE MINMAXAVE
}
