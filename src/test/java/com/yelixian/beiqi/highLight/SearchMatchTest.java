package com.yelixian.beiqi.highLight;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "朵l";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "朵la";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "duo拉zh";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "duola";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "duolaa";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉之哆啦A梦";
        childChinese = "dl";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵 拉之哆啦A梦";
        childChinese = "d l";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵 拉之哆啦A梦";
        childChinese = "dl";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "爱冒险的朵拉 第8季";
        childChinese = "ola d";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "朵拉A梦";
        childChinese = "duola";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "宝宝巴士儿歌之我爱爸爸";
        childChinese = "bb";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "宝宝巴士儿歌之我爱爸爸";
        childChinese = "爸";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);

        logger.info("-------------------------------------");
        parentChinese = "宝宝巴士儿歌之我爱爸爸";
        childChinese = "宝";
        ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
    }

    @Test
    public void test2() {
        String parentChinese = "爱冒险的 duola 之哆啦A梦";
        String childChinese = "a";
        //System.out.println("\u0322");
        String ans = HighlightUtils.makeHighlight(parentChinese, childChinese);
        logger.info("parent: " + parentChinese);
        logger.info("child: " + childChinese);
        logger.info("ans: " + ans);
    }

}