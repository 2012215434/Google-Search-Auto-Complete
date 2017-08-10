package webDemo;

import java.util.List;

/**
 * Created by zhangsheng on 8/9/17.
 */
public class FollowingWord {
    private String startPhrase;
    private String followingWord;
    private int count;

    public String getStartPhrase() {
        return startPhrase;
    }

    public void setStartPhrase(String startPhrase) {
        this.startPhrase = startPhrase;
    }

    public FollowingWord(String startPhrase, String followingWord, int count) {
        this.startPhrase = startPhrase;
        this.followingWord = followingWord;
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFollowingWord() {
        return followingWord;
    }

    public void setFollowingWord(String followingWord) {
        this.followingWord = followingWord;
    }
}
