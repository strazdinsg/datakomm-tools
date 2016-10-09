package no.ntnu.datakomm.lab04;

import java.util.Random;

/**
 * A simple task requiring to send a static message "Hello" to the server
 *
 * @author Girts Strazdins, 2016-10-09
 */
public class EchoTask extends Task {

    public EchoTask() {
        super();
        this.id = Tasks.TASK_ECHO.ordinal();
        this.description = "You should read the text which is in the argument[0]"
                + "and echo it back: send an HTTP post with a "
                + "parameter response=receivedMessage";
        String message = pickRandomQuote();
        this.arguments.add(message);
    }

    static final String[] QUOTES = {
        "J2ME programmers count bytes the way a super-model counts calories. – Unknown",
        "Java: write once, run away! –Brucee",
        "If Java had true garbage collection, most programs would delete themselves upon execution. – Robert Sewell",
        "Saying that Java is good because it works on all platforms is like saying anal sex is good because it works on all genders. – Unknown",
        "Java is a DSL to transform big XML documents into long exception stack traces. – Scott Bellware",
        "The definition of Hell is working with dates in Java, JDBC, and Oracle. Every single one of them screw it up. – Dick Wall CommunityOne 2007: Lunch with the Java Posse",
        "There are two ways of constructing a software design: One way is to make it so simple that there are obviously no deficiencies, and the other way is to make it so complicated that there are no obvious deficiencies. The first method is far more difficult. – C.A.R. Hoare (British computer scientist, winner of the 1980 Turing Award)",
        "If debugging is the process of removing software bugs, then programming must be the process of putting them in. – Edsger Dijkstra (Dutch computer scientist, winner of the 1972 Turing Award)",
        "Measuring programming progress by lines of code is like measuring aircraft building progress by weight. – Bill Gates",
        "Debugging is twice as hard as writing the code in the first place. Therefore, if you write the code as cleverly as possible, you are, by definition, not smart enough to debug it. – Brian W. Kernighan",
        "Always code as if the guy who ends up maintaining your code will be a violent psychopath who knows where you live. – Martin Golding",
        "When debugging, novices insert corrective code; experts remove defective code. – Richard Pattis",
        "Computer science education cannot make anybody an expert programmer any more than studying brushes and pigment can make somebody an expert painter. – Eric S. Raymond (American programmer, open source software advocate, author of “The Cathedral and the Bazaar”)",
        "Most good programmers do programming not because they expect to get paid or get adulation by the public, but because it is fun to program.- Linus Torvalds",
        "Programming today is a race between software engineers striving to build bigger and better idiot-proof programs, and the Universe trying to produce bigger and better idiots. So far, the Universe is winning. – Rich Cook",
        "Any fool can write code that a computer can understand. Good programmers write code that humans can understand.- Martin Fowler",
        "Good code is its own best documentation. As you’re about to add a comment, ask yourself, ‘How can I improve the code so that this comment isn’t needed?’ – Steve McConnell",
        "One of my most productive days was throwing away 1000 lines of code. – Ken Thompson",
        "Most software today is very much like an Egyptian pyramid with millions of bricks piled on top of each other, with no structural integrity, but just done by brute force and thousands of slaves. – Alan Kay",
        "Before software can be reusable it first has to be usable. – Ralph Johnson",
        "If builders built buildings the way programmers wrote programs, then the first woodpecker that came along wound destroy civilization. – Gerald Weinberg",
        "It should be noted that no ethically-trained software engineer would ever consent to write a DestroyBaghdad procedure. Basic professional ethics would instead require him to write a DestroyCity procedure, to which Baghdad could be given as a parameter. – Nathaniel Borenstein",
        "No matter how slick the demo is in rehearsal, when you do it in front of a live audience the probability of a flawless presentation is inversely proportional to the number of people watching, raised to the power of the amount of money involved. –Mark Gibbs",
        "Don’t include a single line in your code which you could not explain to your grandmother in a matter of two minutes. – Unknown",
        "The best thing about a boolean is even if you are wrong, you are only off by a bit. – Bryan",
        "Good design adds value faster than it adds cost.",
        "Simplicity and elegance are unpopular because thy require hard work and discipline to achieve and education to be appreciated. — Edsger Dijkstra",
        "Cherish your exceptions.",
        "A complex system that works is invariably found to have evolved from a simple system that worked.",
        "Walking on water and developing software from a specification are easy if both are frozen.  – Edward V Berard",
        "First, solve the problem. Then, write the code. – John Johnson"
    };
    
    /**
     * Pick one quote at random
     * @return 
     */
    private String pickRandomQuote() {
        Random r = new Random();
        int quoteId = r.nextInt(QUOTES.length);
        return QUOTES[quoteId];
    }
}
