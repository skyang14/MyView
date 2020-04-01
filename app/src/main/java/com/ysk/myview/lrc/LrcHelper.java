package com.ysk.myview.lrc;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yang.shikun on 2020/4/1 13:58
 */

public class LrcHelper {
    private static final String TAG = "LrcHelper";
    private static final String CHARSET = "utf-8";
    //[03:56.00][03:18.00][02:06.00][01:07.00]原谅我这一生不羁放纵爱自由
    private static final String LINE_REGEX = "((\\[\\d{2}:\\d{2}\\.\\d{2}])+)(.*)";
    private static final String TIME_REGEX = "\\[(\\d{2}):(\\d{2})\\.(\\d{2})]";

    public static List<Lrc> parseLrcFromAssets(Context context, String fileName) {
        try {
            return parseInputStream(context.getResources().getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Lrc> parseLrcFromFile(File file) {
        try {
            return parseInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Lrc> parseInputStream(InputStream inputStream) {
        List<Lrc> lrcs = new ArrayList<>();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(inputStream, CHARSET);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                List<Lrc> lrcList = parseLrc(line);
                if (lrcList != null && lrcList.size() != 0) {
                    lrcs.addAll(lrcList);
                }
            }
            sortLrcs(lrcs);
            return lrcs;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return lrcs;
    }

    private static void sortLrcs(List<Lrc> lrcs) {
        Collections.sort(lrcs, new Comparator<Lrc>() {
            @Override
            public int compare(Lrc o1, Lrc o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });
    }

    private static List<Lrc> parseLrc(String lrcLine) {
        if (lrcLine.trim().isEmpty()) {
            return null;
        }
        List<Lrc> lrcs = new ArrayList<>();
        Matcher matcher = Pattern.compile(LINE_REGEX).matcher(lrcLine);
        if (!matcher.matches()) {
            return null;
        }

        String time = matcher.group(1);
        String content = matcher.group(3);
        Matcher timeMatcher = Pattern.compile(TIME_REGEX).matcher(time);

        while (timeMatcher.find()) {
            String min = timeMatcher.group(1);
            String sec = timeMatcher.group(2);
            String mil = timeMatcher.group(3);
            Lrc lrc = new Lrc();
            if (content != null && content.length() != 0) {
                lrc.setTime(Long.parseLong(min) * 60 * 1000 + Long.parseLong(sec) * 1000
                        + Long.parseLong(mil) * 10);
                lrc.setText(content);
                lrcs.add(lrc);
            }
        }
        return lrcs;
    }

    /**
     * 传入的参数为标准歌词字符串
     * @param lrcStr
     * @return
     */
    public static List<Lrc> parseStr2List(String lrcStr) {
        List<Lrc> list = new ArrayList<>();
        String lrcText = lrcStr.replaceAll("&#58;", ":")
                .replaceAll("&#10;", "\n")
                .replaceAll("&#46;", ".")
                .replaceAll("&#32;", " ")
                .replaceAll("&#45;", "-")
                .replaceAll("&#13;", "\r")
                .replaceAll("&#39;", "'")
                .replace(" ","");
        String[] split = lrcText.split("\n");
        for (int i = 0; i < split.length; i++) {
            String lrc = split[i];
            if (lrc.contains(".")) {
                String min = lrc.substring(0,2);
                Log.e(TAG, "parseStr2List:min== "+min );
                String seconds = lrc.substring(lrc.indexOf(":") + 1, lrc.indexOf(":") + 3);
                Log.e(TAG, "parseStr2List:seconds== "+seconds );
                String mills = lrc.substring(lrc.indexOf(".") + 1, lrc.indexOf(".") + 3);
                Log.e(TAG, "parseStr2List:mills== "+mills );
                String text = lrc.substring(8);
                if (text == null || "".equals(text)) {
                    text = "music";
                }
                Lrc lrc1 = new Lrc();
                lrc1.setTime(Long.parseLong(min) * 60 * 1000 + Long.parseLong(seconds) * 1000
                        + Long.parseLong(mills) * 10);
                lrc1.setText(text);
                list.add(lrc1);
            }
        }
        return list;
    }

    public static String formatTime(long time) {
        int min = (int) (time / 60000);
        int sec = (int) (time / 1000 % 60);
        return adjustFormat(min) + ":" + adjustFormat(sec);
    }

    private static String adjustFormat(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return time + "";
    }
}
