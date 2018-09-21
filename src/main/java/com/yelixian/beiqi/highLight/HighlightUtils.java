package com.yelixian.beiqi.highLight;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightUtils {

    private static final Logger logger = LoggerFactory.getLogger(HighlightUtils.class);
    private static final int MAXLENGTH = 100;
    private static final String CUT_CHAR = "\\u0322";
    private static final String ADD_CHAR = "\u0322";
    private static final String HTML_PREFIX = "<b>";
    private static final String HTML_SUFFIX = "</b>";

    public static String makeHighlight(String parentChinese, String childChinese) {
        if (StringUtils.isBlank(parentChinese) || StringUtils.isBlank(childChinese)) {
            return parentChinese;
        }
        parentChinese = parentChinese.trim();
        childChinese = childChinese.trim();

        try {
            int[] ans1 = matchChinese(parentChinese, childChinese);
            //获取最终字符串
            if (ans1[0] != -1) {
                return getAnsStr(parentChinese, ans1);
            }
            //logger.info("父字符串汉字 " + parentChinese);
            // 获取汉字的拼音（有空格）
            String parentEnglish = transPinyin(parentChinese);
            String childEnglish = transPinyin(childChinese);

            if (parentEnglish.length() < childEnglish.length()) {
                return parentChinese;
            }


            //logger.info("父字符串拼音 " + parentEnglish);
            //logger.info("子字符串拼音 " + childEnglish);
            //获取拼音对应的汉字的下标
            int[] englishPKChinses = getIndex(parentEnglish);
            //logger.info("englishPKChinses = " + Arrays.toString(englishPKChinses));
            //int[] ans2 = matchEnglish(parentEnglish, childEnglish, englishPKChinses);

            //无空格的拼音
            String noSpaceParentEnglish = parentEnglish.replaceAll(CUT_CHAR, "");
            String noSpaceChildEnglish = childEnglish.replaceAll(CUT_CHAR, "");
            //logger.info("父字符串无特殊字符拼音 " + noSpaceParentEnglish);
            //logger.info("子字符串无特殊字符拼音 " + noSpaceChildEnglish);
            //全拼音
            int[] ans2 = matchEnglish(noSpaceParentEnglish, noSpaceChildEnglish, englishPKChinses);
            //logger.info("ans2 = " + Arrays.toString(ans2));

            //logger.info("ans2=" + Arrays.toString(ans2));
            //汉字加拼音的特殊情况，且汉字重音
            //char[] parentChars = parentChinese.toCharArray();
            //首字母，先判断子字符串是否有中文，如果有就不需要判断首字母
            int[] ans3 = {-1};
            if (!haveChinese(childChinese)) {
                String parentFirstChar = getFirstChar(parentEnglish);
                //logger.info("父字符串首字母 " + parentFirstChar);
                ans3 = matchNoChinese(parentFirstChar, childChinese);
                //logger.info("ans3 = " + Arrays.toString(ans3));
            }

            //当两个都有值时，拼接两个结果
            if (ans2[0] != -1 && ans3[0]!=-1) {
                int[] ansResult = joinStr(ans2, ans3);
                ansResult = matchPartChinese(parentChinese, childChinese, ansResult);
                return getAnsStr(parentChinese, ansResult);
            }
            if (ans2[0] != -1) {
                ans2 = matchPartChinese(parentChinese, childChinese, ans2);
                return getAnsStr(parentChinese, ans2);
            }
            if (ans3[0] != -1) {
                ans3 = matchPartChinese(parentChinese, childChinese, ans3);
                return getAnsStr(parentChinese, ans3);
            }
        } catch (Exception e) {
            logger.error("设置高亮失败：", e);
        }

        return parentChinese;
    }

    //拼接两个结果
    private static int[] joinStr(int[] ans2, int[] ans3) {
        int[] ansResult = new int[MAXLENGTH];
        initMap(ansResult);
        int resultIndex = 0;
        int index2 = 0;
        int index3 = 0;
        while (ans2[index2] != -1 || ans3[index3] != -1) {
            while(ans2[index2] == -1 && ans3[index3] != -1) {
                for (int i=0; i<2; i++) {
                    ansResult[resultIndex++] = ans3[index3++];
                }
            }
            while(ans3[index3] == -1 && ans2[index2] != -1) {
                for (int i=0; i<2; i++) {
                    ansResult[resultIndex++] = ans2[index2++];
                }
            }
            if (ans2[index2] < ans3[index3]) {
                //2的右边小于3的左边，直接添加
                if (ans2[index2+1] < ans3[index3]) {
                    ansResult[resultIndex++] = ans2[index2++];
                    ansResult[resultIndex++] = ans2[index2++];
                } else {
                    //2的右边大于3的左边，找到最大的全包
                    ansResult[resultIndex++] = ans2[index2];
                    ansResult[resultIndex++] = Math.max(ans2[index2+1], ans3[index3+1]);
                    index2+=2;
                    index3+=2;
                }
            } else {
                //同上
                if (ans3[index3+1] < ans2[index2]) {
                    ansResult[resultIndex++] = ans3[index3++];
                    ansResult[resultIndex++] = ans3[index3++];
                } else {
                    //2的右边大于3的左边，找到最大的全包
                    ansResult[resultIndex++] = ans3[index3];
                    ansResult[resultIndex++] = Math.max(ans2[index2+1], ans3[index3+1]);
                    index2+=2;
                    index3+=2;
                }
            }
        }
        return ansResult;
    }

    //汉字加拼音的特殊情况，且汉字重音
    private static int[] matchPartChinese(String parentChinese, String childChinese, int[] ansTest) {
        char[] childChars = childChinese.toCharArray();
        if (ansTest[0] != -1) {
            int[] ansResult = new int[MAXLENGTH];
            initMap(ansResult);
            for (char indexChar=0; indexChar<childChars.length; indexChar++) {
                int resultIndex = 0;
                int ansIndex = 0;
                //如果有汉字就查询该汉字是否在查询的结果中，包含则留下结果否则就丢弃
                if (childChars[indexChar] >= 0X4e00 && childChars[indexChar] <= 0X9fa5) {
                    //logger.info("parentChars[indexChar] = " + Character.toString(childChars[indexChar]));
                    int[] ansTemp = matchNoChinese(parentChinese, Character.toString(childChars[indexChar]));
                    //logger.info("ansTemp = " + Arrays.toString(ansTemp));
                    //logger.info("ansTest = " + Arrays.toString(ansTest));
                    int tempIndex = 0;
                    //以单个汉字查询结果为结束标志
                    while (ansIndex<MAXLENGTH && tempIndex < MAXLENGTH && ansTemp[tempIndex] != -1 && ansTest[ansIndex] != -1) {
                        //寻找左边下标
                        //logger.info("ansTest[ansIndex] = " + ansTest[ansIndex]);
                        //logger.info("ansTemp[tempIndex] = " + ansTemp[tempIndex]);
                        while (tempIndex < MAXLENGTH && ansTest[ansIndex] > ansTemp[tempIndex]) {
                            tempIndex++;
                        }
                        if (tempIndex >= MAXLENGTH) break;
                        //比对右边下标是否包括
                        int startIndex = ansIndex;
                        if (ansTest[++ansIndex] >= ansTemp[++tempIndex]) {
                            ansResult[resultIndex++] = ansTest[startIndex];
                            ansResult[resultIndex++] = ansTest[ansIndex];
                        }
                        //logger.info("ansResult = " + Arrays.toString(ansResult));
                        //下标增加
                        ansIndex++;
                        tempIndex++;
                    }
                    //结果数组拷贝到原数组
                    ansTest = Arrays.copyOf(ansResult, ansResult.length);
                    initMap(ansResult);
                }
                if (ansTest[0] == -1) {
                    break;
                }
            }
        }
        return ansTest;
    }

    //添加高亮代码
    private static String getAnsStr(String parentChinese, int[] ans1) {
        int index = 0;
        int firstE = 0;
        boolean haveEnglish = false;
        int[] ans3 = new int[MAXLENGTH];
        int[] ans2 = getChineseIndex(parentChinese);
        for (int i=0; ans1[i]!=-1; i++) {
            //logger.info("ans  " + ans1[i] + " | " + ans2[ans1[i]]);
            ans3[i] = ans1[i];
            ans1[i] = ans2[ans1[i]];
        }
        //logger.info("emmm");
        StringBuilder ansSb = new StringBuilder(parentChinese);
        ansSb.append("*");
        //logger.info("ans3 = " + Arrays.toString(ans3));
        //logger.info("ans1 = " + Arrays.toString(ans1));
        //加入html长度
        int length = 0;
        //英语的长度
        int eLength;
        while (ans1[index] != -1) {

            ansSb.insert(ans1[index++]+length, HTML_PREFIX);
            length += HTML_PREFIX.length();
            firstE = ans1[index];
            //logger.info("firstE = " + index);
            // 考虑最后一个是不是英语
            //logger.info("1 ansSb.charAt(firstE+length) = " + ansSb.charAt(firstE+length+eLength));
            eLength = 0;
            while ((ansSb.charAt(firstE+length+eLength)>=0x0041 && ansSb.charAt(firstE+length+eLength)<=0x005A) ||
                    (ansSb.charAt(firstE+length+eLength)>=0x0061 && ansSb.charAt(firstE+length+eLength)<=0x007A)) {
                eLength++;
                if (firstE + eLength == ansSb.length()) {
                    break;
                }
            }
            if (eLength != 0) {
                eLength--;
            }
            //logger.info(" 略略略ansSb.charAt(firstE+length) = " + ansSb.charAt(firstE+length));
            //logger.info("ansSb = " + ansSb.toString());
            //logger.info("ans1[index] = " + ans1[index]);
            //logger.info("ans1[index++]+length+1 = " + ans1[index]+length+1+eLength);
            ansSb.insert(ans1[index++]+length+1+eLength, HTML_SUFFIX);

            length += HTML_SUFFIX.length();
        }
        ansSb.deleteCharAt(ansSb.length()-1);
        return ansSb.toString();
    }

    //左闭右闭，用int数组保存下标
    private static int[] matchChinese(String parent, String child) {
        int index = 0;
        int firstIndex = 0;
        int length = child.length();
        int[] map = new int[MAXLENGTH];
        initMap(map);
        int mapIndex = 0;
        while((index = parent.indexOf(child, index)) != -1 )
        {
            //防止字符串匹配部分字符
            if (index > 0 && index+length<=parent.length()) {
                if (child.matches("^[a-zA-Z]+$")) {
                    String temp = parent.substring(index-1, index);
                    if (temp.matches("^[a-zA-Z]+$")) {
                        index = index+length;
                        continue;
                    }
                    temp = parent.substring(index+length-1, index+length);
                    if (temp.matches("^[a-zA-Z]+$")) {
                        index = index+length;
                        continue;
                    }
                }
            }

            firstIndex = index;
            index = index+length;
            map[mapIndex++] = firstIndex;
            map[mapIndex++] = index-1;
        }

        /*for (int i=0; i<mapIndex; ) {
            System.out.println(map[i++] + "|" + map[i++]);
        }*/

        return map;
    }

    //左闭右闭，用int数组保存下标, 匹配没有连续的英文
    private static int[] matchNoChinese(String parent, String child) {
        int index = 0;
        int firstIndex = 0;
        int length = child.length();
        int[] map = new int[MAXLENGTH];
        initMap(map);
        int mapIndex = 0;
        while((index = parent.indexOf(child, index)) != -1 )
        {
            firstIndex = index;
            index = index+length;
            map[mapIndex++] = firstIndex;
            map[mapIndex++] = index-1;
        }
        return map;
    }


    //转成拼音比较， 拼音下标与汉字下标对应
    private static int[] matchEnglish(String parent, String child, int[] englishPKChinses) {
        int[] mapTemp = matchNoChinese(parent, child);
        int[] map = new int[MAXLENGTH];
        initMap(map);
        int mapIndex = 0;
        while (mapTemp[mapIndex] != -1) {
            //必须首字母开始，末尾字母不算
            if (mapTemp[mapIndex]>0 && englishPKChinses[mapTemp[mapIndex]] == englishPKChinses[mapTemp[mapIndex]-1]) {
                mapIndex+=2;
                continue;
            } else {
                for (int i=0; i<2; i++) {
                    map[mapIndex] = englishPKChinses[mapTemp[mapIndex++]];
                }
            }
        }
        return map;
    }



    private static String transPinyin(String string){

        StringBuilder outputBuilder = null;
        try {
            // 创建format
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            // 设置format为小写的格式
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            // 设置format为带音调的格式
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            // 设置format为ü的显示格式
            format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

            // 将string转换成char数组
            char[] preTransChars = string.toCharArray();
            // 定义一个接收转换后的StringBuilder变量
            outputBuilder = new StringBuilder();

            // for
            int length = preTransChars.length;
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<length;i++){
                // 如果当前字符是中文，将转换成汉语拼音字符数组
                if(Character.toString(preTransChars[i]).matches("[\u4E00-\u9FA5]+")){
                    String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(preTransChars[i], format);
                    //有缓存的字符串先弄进去
                    if (!sb.toString().equals("")) {
                        outputBuilder.append(sb.toString()).append(ADD_CHAR);
                        sb.delete(0, sb.length());
                    }
                    outputBuilder.append(pinyins[0]).append(ADD_CHAR);
                }else{
                    // 如果不是中文，则不转换直接拼接,转小写先
                    preTransChars[i] = Character.toLowerCase(preTransChars[i]);
                    if (preTransChars[i] >= 0x0061 && preTransChars[i] <= 0x007A) {
                        sb.append(preTransChars[i]);
                        continue;
                    }
                    if (!sb.toString().equals("")) {
                        outputBuilder.append(sb.toString()).append(ADD_CHAR);
                        sb.delete(0, sb.length());
                    }
                    outputBuilder.append(Character.toString(preTransChars[i])).append(ADD_CHAR);
                }
            }
            //最后缓存的一些字符
            if (!sb.toString().equals("")) {
                outputBuilder.append(sb.toString()).append(ADD_CHAR);
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        return outputBuilder.toString();
    }


    private static int[] getIndex(String pingying){
        String[] hanzis = pingying.split(CUT_CHAR);
        int[] englishPKChinses = new int[100];
        //拼音下标
        int indexE = 0;
        //汉字下标
        int indexC = 0;
        //拼音下标匹配汉字下标
        for (String hanzi : hanzis) {
            int hanziLength = hanzi.length();
            for (int i=indexE; i<indexE+hanziLength; i++) {
                englishPKChinses[i] = indexC;
            }
            indexE += hanziLength;
            indexC++;
        }
        englishPKChinses[indexE] = indexC;
        /*//测试输出
        System.out.println(pingying);
        for (int i=0; i<=indexE; i++) {
            System.out.println("拼音下标=" + i + " | 汉字下标="+englishPKChinses[i]);
        }
        System.out.println(indexC);*/
        return englishPKChinses;
    }

    public static int[] getChineseIndex(String str) {
        char[] hanzis = str.toCharArray();
        int[] chinesePKEnglish = new int[100];
        boolean hasE = false;
        //拼音下标
        int indexE = 0;
        //汉字下标
        int indexC = 0;
        while (indexE < hanzis.length){

            chinesePKEnglish[indexC] = indexE;
            //logger.info("hanzis[indexE] = " + hanzis[indexE]);
            //如果是英语的话要连起来
            while ((hanzis[indexE]>=0x0041 && hanzis[indexE]<=0x005A) || (hanzis[indexE]>=0x0061 && hanzis[indexE]<=0x007A)) {
                indexE++;
                hasE = true;
                if(indexE >= hanzis.length) {
                    break;
                }
            }
            if (!hasE) {
                indexE++;
            }
            hasE = false;
            indexC++;
        }
        chinesePKEnglish[indexC] = -1;
        /*//测试输出
        for (int i=0; i<indexC; i++) {
            System.out.println("汉字下标=" + i + " | 拼音下标=" + chinesePKEnglish[i]);
        }
        System.out.println(indexC);*/
        return chinesePKEnglish;
    }


    private static void initMap(int[] map) {
        for (int i=0; i<MAXLENGTH; i++) {
            map[i] = -1;
        }
    }

    private static String getFirstChar(String pingying) {
        String[] hanzis = pingying.split(CUT_CHAR);
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<hanzis.length; i++) {
            sb.append(hanzis[i].charAt(0));
        }
        return sb.toString();
    }

    //判断字符串是否含有中文
    private static boolean haveChinese(String str) {
        String test = "[\\u4E00-\\u9FA5]+";
        Pattern p = Pattern.compile(test);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
