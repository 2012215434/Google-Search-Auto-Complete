package webDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangsheng on 8/9/17.
 */
@RestController
public class WebDemoController {

    @Autowired
    FollowingWordRepository followingWordRepository;

    @RequestMapping(value = "/completions/{startPhrase}")
    public List<FollowingWord> completions(@PathVariable("startPhrase") String startPhrase) {
        return followingWordRepository.searchCompletion(startPhrase);
    }
}
