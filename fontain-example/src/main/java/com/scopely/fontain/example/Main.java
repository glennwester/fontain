package com.scopely.fontain.example;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.android.internal.util.Predicate;
import com.scopely.fontain.Fontain;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.spans.FontSpan;
import com.scopely.fontain.views.FontTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;


public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fontain.init(this, getString(R.string.default_font_family));
        Fontain.addFontFamilyFromDir("MedievalSharp", mockDir());
        Fontain.applyFontToViewHierarchy(getActionBarView(), Fontain.getFontFamily("MedievalSharp"), Weight.NORMAL, Width.NORMAL, Slope.NORMAL);
        setContentView(R.layout.activity_main);

        ViewGroup container = (ViewGroup) findViewById(R.id.textViewContainer);
        rotateFonts(container);

        TextView spannedTextView = (TextView) findViewById(R.id.spannedTextView);
        spannedTextView.setText(getFontSpannableExampleText());

        ViewGroup predicateContainer = (ViewGroup) findViewById(R.id.predicateContainer);
        setFontWithPredicate(predicateContainer);
    }

    private File mockDir() {
        File dir = new File(getCacheDir(),"MedievalSharp");
        if (!dir.exists()) try {
            dir.mkdirs();
            for(String path : getAssets().list("extra/MedievalSharp")){
                File f = new File(dir, path);
                InputStream is = getAssets().open("extra/MedievalSharp/"+path);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
            }

        } catch (Exception e) { throw new RuntimeException(e); }
        return dir;
    }

    /**
     *
     * Assigns the MedievalSharp font family to any TextView within root that matches the predicate.
     * The predicate in this case is if the TextView's text is the string 'Yes'
     *
     * @param root
     */
    private void setFontWithPredicate(ViewGroup root) {
        Fontain.applyFontFamilyToViewHierarchy(root, Fontain.getFontFamily("MedievalSharp"), new Predicate<TextView>() {
            @Override
            public boolean apply(TextView textView) {
                return "Yes".equals(textView.getText().toString());
            }
        });
    }

    private CharSequence getFontSpannableExampleText() {
        String string = getString(R.string.three_different_fontspans);
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new FontSpan(Fontain.getFontFamily("MedievalSharp").getFont(Weight.NORMAL, Width.NORMAL, Slope.NORMAL)), 0, 5, 0);
        spannableString.setSpan(new FontSpan(Fontain.getFontFamily("LS").getFont(Weight.NORMAL, Width.NORMAL, Slope.NORMAL)), 6, 15, 0);
        spannableString.setSpan(new FontSpan(Fontain.getFontFamily("PTSans").getFont(Weight.NORMAL, Width.NORMAL, Slope.NORMAL)), 16, 24, 0);
        return spannableString;
    }

    /**
     *
     * Assigns a new Font Family to the container view hierarchy every three seconds.
     * Used to demonstrate how a Font Family can be applied while maintaining weight/width/italic values of each TextView in the hierarchy.
     * Even when using a Font Family that doesn't have a suitable matching font for a given weight/width/italic combination, the values are maintained should a Font Family with a suitable match later be assigned to the TextView.
     *
     * @param container
     */
    private void rotateFonts(final ViewGroup container) {
        final Iterator<String> iterator = new Iterator<String>() {
            final String[] fontFamilies = new String[]{"MedievalSharp", "LS", "PTSans"};
            int index = 0;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                index++;
                if(index >= fontFamilies.length) index = 0;
                return fontFamilies[index];
            }

            @Override
            public void remove() {
                //no op
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Fontain.applyFontFamilyToViewHierarchy(container, Fontain.getFontFamily(iterator.next()));
                container.postDelayed(this, 3000);
            }
        };
        runnable.run();
    }

    public View getActionBarView() {
        Window window = getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
    }
}
