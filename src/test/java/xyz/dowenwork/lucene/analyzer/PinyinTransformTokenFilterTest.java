package xyz.dowenwork.lucene.analyzer;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * <p>create at 16-6-3</p>
 *
 * @author liufl
 * @since 5.5.0.0
 */
public class PinyinTransformTokenFilterTest extends BaseTokenStreamTestCase {

    private MockTokenizer tokenizer;
    private PinyinTransformTokenFilter filter;

    @Before
    public void before() throws IOException {
        this.tokenizer = new MockTokenizer();
        this.tokenizer.setReader(new StringReader("和平 重量 and 中国 中華人民共和國 凪のあすから 降省给"));
    }

    private int _innerTest() throws IOException {
        int position = 0;
        while (this.filter.incrementToken()) {
            CharTermAttribute termAtt = this.filter.getAttribute(CharTermAttribute.class);
            String token = termAtt.toString();
            int increment = this.filter.getAttribute(PositionIncrementAttribute.class).getPositionIncrement();
            position += increment;
            OffsetAttribute offset = this.filter.getAttribute(OffsetAttribute.class);
            TypeAttribute type = this.filter.getAttribute(TypeAttribute.class);
            System.out.println(position + "[" + offset.startOffset() + "," + offset.endOffset() + "} (" + type
                    .type() + ") " + token);
        }
        return position;
    }

    @Test
    public void testFull() throws IOException {
        this.filter = new PinyinTransformTokenFilter(tokenizer);
        this.filter.reset();
        int position = _innerTest();
        assertTrue(position == 7);
    }

    @Test
    public void testFullWithNoChineseOut() throws IOException {
        this.filter = new PinyinTransformTokenFilter(tokenizer, PinyinTransformTokenFilter.TYPE_PINYIN, 1, false);
        this.filter.reset();
        int position = _innerTest();
        System.err.println(position);
        assertTrue(position == 5);
    }

    @Test
    public void testShort() throws IOException {
        this.filter = new PinyinTransformTokenFilter(tokenizer, PinyinTransformTokenFilter.TYPE_ABBREVIATION);
        this.filter.reset();
        int position = _innerTest();
        assertTrue(position == 7);
    }

    @Test
    public void testPolyphone() throws IOException{
        this.filter = new PinyinTransformTokenFilter(tokenizer, PinyinTransformTokenFilter.TYPE_ABBREVIATION,2,1,true);
        this.filter.reset();
        int position = _innerTest();
        assertTrue(position == 7);
    }
}
