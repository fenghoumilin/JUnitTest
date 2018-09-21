package com.yelixian.beiqi.highLight;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchMatchTest {
    private static final Logger logger = LoggerFactory.getLogger(SearchMatchTest.class);

    @Test
    public void test() {
        String parentChinese = "爱冒险的朵拉之哆啦A梦";
        String childChinese = "朵拉";
        //System.out.println("\u0322");
        String ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的<b>朵拉</b>之哆啦A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "朵l";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的<b>朵拉</b>之哆啦A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "朵la";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的<b>朵拉</b>之哆啦A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "duo拉zh";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的<b>朵拉之</b>哆啦A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "duola";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的<b>朵拉</b>之<b>哆啦</b>A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "duolaa";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的朵拉之<b>哆啦A</b>梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "dl";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的<b>朵拉</b>之<b>哆啦</b>A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵 拉之哆啦A梦";
        childChinese = "d l";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的<b>朵 拉</b>之哆啦A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵 拉之哆啦A梦";
        childChinese = "dl";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的朵 拉之<b>哆啦</b>A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉 第8季";
        childChinese = "ola d";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱冒险的朵拉 第8季", ans);

        logger.info("-------------------------------------");
        parentChinese = "朵拉A梦";
        childChinese = "duola";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("<b>朵拉</b>A梦", ans);

        logger.info("-------------------------------------");
        parentChinese = "宝宝巴士儿歌之我爱爸爸";
        childChinese = "bb";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("<b>宝宝</b>巴士儿歌之我爱<b>爸爸</b>", ans);

        logger.info("-------------------------------------");
        parentChinese = "宝宝巴士儿歌之我爱爸爸";
        childChinese = "爸";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("宝宝巴士儿歌之我爱<b>爸</b><b>爸</b>", ans);

        logger.info("-------------------------------------");
        parentChinese = "宝宝巴士儿歌之我爱爸爸";
        childChinese = "宝";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("<b>宝</b><b>宝</b>巴士儿歌之我爱爸爸", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱探险的朵拉之哆啦A梦first part";
        childChinese = "f p";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱探险的朵拉之哆啦A梦<b>first part</b>", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱探险的朵拉之哆啦A梦first part";
        childChinese = "fir";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱探险的朵拉之哆啦A梦<b>first</b> part", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱探险的朵拉之哆啦A梦first part";
        childChinese = "a";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("<b>爱</b>探险的朵拉之哆啦<b>A</b>梦first part", ans);

        logger.info("-------------------------------------");
        parentChinese = "海尔兄弟20周年纪念版";
        childChinese = "20";
        //System.out.println("\u0322");
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("海尔兄弟<b>20</b>周年纪念版", ans);

    }

    @Test
    public void test2() {
        String parentChinese = "爱探险的朵拉之哆啦A梦";
        String childChinese = "梦";
        String ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱探险的朵拉之哆啦A梦first part";
        childChinese = "p";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱探险的朵拉之哆啦A梦first <b>part</b>", ans);

        logger.info("-------------------------------------");
        parentChinese = "爱探险的朵拉之哆啦A梦first part";
        childChinese = "f p";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
        Assert.assertEquals("爱探险的朵拉之哆啦A梦<b>first part</b>", ans);

    }

    @Test
    public void test3() {
        String child = "wdsadas  我Pasda";
        child = child.toLowerCase();
        StringBuilder sb = new StringBuilder();
        logger.info("|"+sb.toString()+"|");
        char ch = '盘';
        ch = Character.toLowerCase(ch);
        logger.info("ch = " + ch);
        logger.info(child);
        if (child.matches("^[a-zA-Z]+$")) {
            logger.info(child);
        }
        String str = "ijwu无i哈";
        String test = "[\\u4E00-\\u9FA5]+";
        Pattern p = Pattern.compile(test);
        Matcher m = p.matcher(str);
        if (m.find()) {
            logger.info(m.group());
        }
        HighlightUtils.getChineseIndex("哆啦A梦first part");
    }

}