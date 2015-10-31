import sample.View;

import java.util.logging.Logger;

/**
 * Created by Marcin on 2015-10-28.
 */
public class Start {
    private static Logger logger = Logger.getLogger(Start.class.getName());

    public static void main(String[] args) {
        View.launch(View.class);
    }

}
