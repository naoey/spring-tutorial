package main.java.twitter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TwitterController {
    private Twitter twitter;

    @RequestMapping(path="/twitter/tweets")
    public TwitterQuery tweetSearch(@RequestParam(value="query", defaultValue="Modi") String query,
                                    @RequestParam(value="limit", defaultValue="10") Integer limit) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader("private/twitter.json"));

            String twitterAPIKey = (String) jsonObject.get("consumer_key");
            String twitterAPISecret = (String) jsonObject.get("secret_key");
            String twitterAccessToken = (String) jsonObject.get("access_token");
            String twitterAccessTokenSecret = (String) jsonObject.get("access_token_secret");

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(twitterAPIKey)
                    .setOAuthConsumerSecret(twitterAPISecret)
                    .setOAuthAccessToken(twitterAccessToken)
                    .setOAuthAccessTokenSecret(twitterAccessTokenSecret);
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();
        } catch (IOException e) {
            System.out.println("Error loading Twitter credentials JSON");
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println("Invalid JSON file content");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println("Unhandled error");
            System.out.println(e);
        }

        try {
            Query twitterQuery = new Query(query);
            twitterQuery.setCount((limit > 100) ? 100 : limit);

            List<Status> result = twitter.search(twitterQuery).getTweets();
            ArrayList<String> returnableResult = new ArrayList<String>(result.size());
            for (Status tweet : result) {
                returnableResult.add(tweet.getText());
            }

            return new TwitterQuery(returnableResult, "Success");
        } catch(TwitterException te) {
            System.out.println(te);
            return new TwitterQuery(new ArrayList<String>(0), "Error connecting to Twitter");
        }
    }
}

class TwitterQuery {
    private final ArrayList<String> queryResult;
    private final String status;

    TwitterQuery(ArrayList<String> queryResult, String status) {
        this.queryResult = queryResult;
        this.status = status;
    }

    public ArrayList<String> getQueryResult() {
        return this.queryResult;
    }

    public String getStatus() {
        return this.status;
    }
}