package com.example.grouperapi.service.impl;

import com.example.grouperapi.model.entities.Post;
import com.example.grouperapi.model.entities.enums.PostType;
import com.example.grouperapi.repositories.PostRepository;
import com.example.grouperapi.service.GroupService;
import com.example.grouperapi.service.PostService;
import com.example.grouperapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserService userService;
    private GroupService groupService;

    @Override
    public void seedPosts() {
        if (postRepository.count() == 0) {
            log.info("seeding posts into the database");
            //post 1
            createPost(
                    "user",
                    "linux",
                    "cat and memory usage?",
                    """
                                Something I was thinking about, but not really able to find an answer for with regards to the cat command.
                            Say you have a txt file, that is 100mb in size, and you cat that file.
                                                        
                            cat sample.txt
                                                        
                            Does that file get read into memory first, then outputed line by line. So conceivable it would utilize 100mb of memory until cat finishes, and then memory is freed?
                            Or does it handle it some other way?
                            Thanks!
                                                                                    """
            );

            //post 2
            createPost(
                    "user1",
                    "programming",
                    "Can someone explain CI/CD please?",
                    """
                            Hi everyone!
                            I'm studying and developing with Java at a pretty decent level, and I was wondering how I could improve my workflow. At the moment, almost all of my project use Maven or Gradle for building, and I was wondering how I could automate that. I host my repos on a Gitea instance and today I also installed Drone.
                            Now, the issue is that whenever I search CI, CD, pipelines, Drone, Jenkins... up, all I find are videos and guides that just completely skip what that even means and just go straight into editing random files and stuff I understand nothing about. All I want is for my repo projects to auto-build and maybe run some tests, but I can't find any good starting point that goes through the basics of it. Can anyone ELI5?
                            Thank you so much!
                            PS: I'm ok with switching from Drone to Jenkins if that's more suitable, I just went with it because I'm not using just Java so it could've come in handy for other languages too - and I don't need a guide for that specific software, but literally just an introduction on how that stuff works. Thanks again!
                                                                                    """
            );


            //post 3
            createPost(
                    "user2",
                    "news",
                    "Russia is blowing up bridges in Sievierodonetsk to thwart Ukrainian reinforcements, regional governor says",
                    "https://www.reuters.com/world/europe/russia-blowing-up-bridges-sievierodonetsk-thwart-ukrainian-reinforcements-2022-06-04/"

            );

            //post 4
            createPost(
                    "user3",
                    "bulgaria",
                    "На 3 юни 1395 г. е убит цар Иван Шишман.",
                    """
                            Последните две години от живота си царят прекарва в Никопол и е васал на султана.
                            Иван Шишман вероятно таи надежди за помощ и се подготвя да се включи при подготвяния от Сигизмунд Люксембургски (крал на Унгария и на Свещената Римска империя) кръстоносен поход срещу османците. Има сведения, че Иван Шишман е убит в Никопол на 3 юни 1395 г. след завземането на града от османските нашественици.
                            Най-вероятна причина за атаката на Никополската крепост е заради факта, че цар Иван Шишман влиза в съюз с унгарците и застава на страната и активно помага на влашкия владетел Мирча Стари при неуспешната за османците битка във Влахия при Ровине. В посоченото сражение загиват и османските васали Константин Драгаш - управител на Велбъжд и Крали Марко от Прилеп.
                            Именно след битката при Ровине Баязид се връща на юг от Дунав използвайки кораби на Иван Шишман и при пресичането на реката в района на Никопол, превзел града и пленил българския владетел. Остатъкът от Търновското царство са включени в Османската империя, а цар Иван Срацимир продължава да управлява на територията на Видинското царство до 1396 г.
                            Поклон!
                                                                                    """
            );
        }
    }

    private void createPost(String authorUsername, String groupName, String title, String content) {
        Post post = new Post();
        post.setPostType(PostType.TEXT);
        post.setCreated(Instant.now());
        post.setAuthor(userService.getUserByUsername(authorUsername));
        post.setGroup(groupService.getGroupByName(groupName));
        post.setTitle(title);
        post.setContent(content);
        post.setComments(new ArrayList<>());
        postRepository.save(post);
    }

}
