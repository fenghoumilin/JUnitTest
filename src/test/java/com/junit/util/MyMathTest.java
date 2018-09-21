package com.junit.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(YouMath.class)
public class MyMathTest {

    @Test
    public void findLongest() {

        MyMath mathSpy = Mockito.spy(new MyMath());
        Mockito.when(mathSpy.findLongest("a", 1, "a", 1)).thenReturn(999);
        int sum = mathSpy.add(8, 1);
        System.out.println(mathSpy.findLongest("a", 1, "ab", 2));

    }

    @Test
    public void addPlusTest() {

        MyMath math = new MyMath();
        Class<? extends MyMath> clazz = math.getClass();
        MyMath mock = Mockito.spy(clazz);
        Mockito.doReturn(9).when(mock).add(1, 2);
        int ans = mock.addPlus(1, 2);
        System.out.println(ans);
    }

    @Test
    public void addYoutest() throws Exception{
        MyMath math = new MyMath();
        PowerMockito.mockStatic(YouMath.class);
        PowerMockito.doReturn(0).when(YouMath.class, "addLow");
        int ans = math.addYou(1, 2);
        System.out.println(ans);

    }

    public static String transPinyin(String string) throws BadHanyuPinyinOutputFormatCombination {
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
        StringBuilder outputBuilder = new StringBuilder();

        // for
        int length = preTransChars.length;
        for(int i=0;i<length;i++){
            // 如果当前字符是中文，将转换成汉语拼音字符数组
            if(Character.toString(preTransChars[i]).matches("[\u4E00-\u9FA5]+")){
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(preTransChars[i], format);
                outputBuilder.append(pinyins[0]).append(" ");
            }else{// 如果不是中文，则不转换直接拼接
                outputBuilder.append(Character.toString(preTransChars[i]));
            }
        }

        return outputBuilder.toString();
    }
}