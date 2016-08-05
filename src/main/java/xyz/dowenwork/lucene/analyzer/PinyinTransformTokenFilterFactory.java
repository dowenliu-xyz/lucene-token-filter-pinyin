package xyz.dowenwork.lucene.analyzer;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

/**
 * <p>create at 16-6-3</p>
 *
 * @author liufl
 * @since 5.5.0.0
 */
public class PinyinTransformTokenFilterFactory extends TokenFilterFactory {
    private boolean isOutChinese = true; // 是否输出原中文开关
    private int minTermLength = 2; // 中文词组长度过滤，默认超过2位长度的中文才转换拼音
    /**
     * 多音字出现次数(缩写不同,例如:省->j,x),默认超过10次不再做组合,防止内存溢出,负数不限制
     * <p>So what?</p>
     * <p>如果每个字都有两个读音,那20个多音字字就是2^20=1,048,576种组合,吃枣药丸.</p>
     */
    private int maxPolyphoneFreq = 10; //
    private boolean firstChar = false; // 拼音缩写开关，不论是否输出缩写，均输出全拼

    /**
     * 构造器
     */
    public PinyinTransformTokenFilterFactory(Map<String, String> args) {
        super(args);
        this.isOutChinese = getBoolean(args, "isOutChinese", true);
        this.firstChar = getBoolean(args, "firstChar", false);
        this.minTermLength = getInt(args, "minTermLength", 2);
        this.maxPolyphoneFreq = getInt(args, "maxPolyphoneFreq", 10);
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    public TokenFilter create(TokenStream input) {
        return new PinyinTransformTokenFilter(input,
                this.firstChar ? PinyinTransformTokenFilter.TYPE_BOTH : PinyinTransformTokenFilter.TYPE_PINYIN,
                this.minTermLength, this.maxPolyphoneFreq, this.isOutChinese);
    }
}
