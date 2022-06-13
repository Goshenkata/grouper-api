package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.FullPostInfoDTO;
import com.example.grouperapi.model.dto.PostCreationDTO;
import com.example.grouperapi.model.dto.PostFeedDTO;
import com.example.grouperapi.model.entities.Post;
import com.example.grouperapi.model.entities.enums.PostType;
import com.example.grouperapi.repositories.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private PostRepository postRepository;
    private UserService userService;
    private GroupService groupService;
    private CloudinaryService cloudinaryService;
    private ModelMapper mapper;

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
                                                                                    """,
                    11
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
                                                                                    """,
                    0
            );


            //post 3
            createPost(
                    "user2",
                    "news",
                    "Russia is blowing up bridges in Sievierodonetsk to thwart Ukrainian reinforcements, regional governor says",
                    "https://www.reuters.com/world/europe/russia-blowing-up-bridges-sievierodonetsk-thwart-ukrainian-reinforcements-2022-06-04/",
                    0

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
                                                                                    """,
                    0
            );

            //post 5
            createPost(
                    "user9",
                    "linux",
                    "Why was compiz abandoned?",
                    """
                             I just discovered it and I love it, till now I had to use picom, but it does not have window animations like kwin, but installing kwin pulls a lot other things unlike compiz.
                            Compiz seems to have a hella lot of bells and whistles along with animations that are actually usable, looking at the arch AUR repos, both 0.8 an 0.8 hasn't been updated since quite some time.
                            I get that there are extensions for gnome to do the same. but compiz can be standalone window manager for people like me and there are a lot of people like me who use stuff like bspwm, dwm, openbox, etc.
                            Why do you think compiz was forced to the backstage these days? 
                                                        """,
                    0
            );

            //post 6
            createPost(
                    "user8",
                    "programming",
                    "Any Suggested Python Library / Specific Expertise [Crop Classification from Satellite Images]",
                    """
                            I'm competing in a hackathon that requires me to classify types of crop based on satellite images. I already have some background in machine learning, and they suggest me to have these knowledges beforehand
                                                        
                            - Data Analytics
                            - Image Processing / OpenCV
                            - Artificial Intelligence / Machine Learning / TensorFlow
                            - Satellite / GIS, Geospatial Technology, Remote Sensing
                                                        
                            ---------------------------------------------------------------------------------------------
                                                        
                            Are there any more python libraries / specific topics I need to know more to do well in this hackathon?
                            [OR are there any of their suggested knowledges I shouldn't focus on]""",
                    0);

            //post 7
            createPost(
                    "user2",
                    "programming",
                    "What are good learning resources for connecting desktop apps to websites?",
                    """
                            I am a newbie. But the best way to describe is like Dropbox. They have software that you can place on the desktop that connects to the their website. What resources can I learn to help me to do this?
                                                        """,
                    0
            );

            //post 8
            createPost(
                    "user1",
                    "programming",
                    "Should copy-paste code be avoided?",
                    """
                            So I graduated with cs bachelor's degree last year but am still working on my personal project to get a job. I'm Korean and most Korean companies require Spring Framework experience for entry level java developer roles so I'm making a webapp using Springboot framework. Anyway, I'm almost done with the project but I didn't come up with all the code by myself but referenced many websites and books, then borrowed some codes from various sources and modified them to run on my project. Idk if I should feel ashamed for this or it's a common practice among software engineers (web developers). I understand the logics behind the code I referenced thou. What's your opinons? 
                            """,
                    0
            );

            //post 9
            createPost(
                    "user5",
                    "programming",
                    "Machine Learning Roadmap?",
                    """
                            I want to learn more about data science, especially by the field of machine learning and ai, can you suggest some roadmap and resources about machine learning. I just really don't know where to start when learning machine learning
                            """,
                    0
            );


            //post 10
            createPost(
                    "user1",
                    "programming",
                    "Is it good or bad practice to use your own “boilerplate” or code skeleton ? ",
                    """
                            Kind of a shower thought or epiphany I had today… I learned the term boilerplate from doing HTML in TOP, and only just realized I could be using it for other stuff.
                            Especially in regard to OpenGL/Vulkan (going through OpenGL rn) and set everything up in a really basic sense, and then go in and change or add program-specific requirements.
                            Like with Vulkan in particular there’s no way people write ~1000 new lines of code just to begin a new project right? it seems easier to just change what’s necessary considering how verbose and explicit Vulkan is, it seems really difficult to start that from memory unless you really know the API well lol, maybe you do.
                            Do you do this or something similar?
                                                        """,
                    0
            );


            //post 11
            createPost(
                    "user9",
                    "bulgaria",
                    "Някъв добър бг сайт за филми?",
                    """
                            Някой знае ли откъде мога да гледам family guy (семейният тип) с бг суб/аудио (което и да е от двете), доколкото знам преди са го давали по fox, но почти никакви епизоди не са записани и качени в нета?
                            """,
                    0
            );


            //post 12
            createPost(
                    "user4",
                    "bulgaria",
                    "How much does it cost to rent an apartment in Sofia and Plovdiv for one year?🤨",
                    "",
                    0
            );


            //post 13
            createPost(
                    "user6",
                    "linux",
                    "BREAKING NEWS!!! CONSERVATIVE UBUNTU USERS: THERE ARE LIBERALS INVADING YOUR COMPUTER THROUGH APT!!!!",
                    """
                            AGENDA ALERT!!!:: there are liberals invading your conservative Ubuntu installation through apt packages! Luckily, these are easily identified since all of their package names start with "lib", a shortening of "liberal" (not a very smart method of hiding your agenda...). Here's a step by step guide on cleaning your PC of liberal malware.
                                                        
                            Step 1: Open a terminal.
                            Step 2: Type the command "sudo apt purge lib*" and press enter.
                            Step 3: Enter your password, allow the package manager to clean your system of liberals.
                            Step 4: Reboot into your brand new liberal-free system.
                            Step 5: Enjoy!
                                                        
                            There you go, your brand-new liberal-free Ubuntu system! It should feel faster because there's no more dumb liberals sapping your CPU's intelligence anymore. No more CIA Obama madness on your trusty Pentium rig.
                                                        """,
                    0
            );

            //post 14
            createPost(
                    "user6",
                    "linux",
                    "I don't like systemd.",
                    "I just don't like it.",
                    0
            );


            //post 15
            createPost(
                    "user6",
                    "linux",
                    "No it's not GNU/Linux",
                    """
                            GNU and Linux are not enough to make a full os you still need an init system(such as systemd), bootloader(such as grub or lilo) a shell(such as zsh or bash) and optionally a desktop environment (such as xfce or kde or gnome)
                            so really it is
                            GNU/Linux/Systemd/GRUB/Bash/KDE
                            or as I have recently taken to calling it.
                            Linux+Systemd+GRUB+Bash+KDE
                            """,
                    0
            );


            //post 16
            createPost(
                    "user6",
                    "linux",
                    "It's been 7 months since I started using Linux",
                    """
                            most specifically Mint, and all the result they promised me were evident: I was a lot faster in my computer, got promoted at work, my IQ increased in at least 1 digit, my wife started looking at me like a man, my erections lasted twice as long and everyone started to respect me, even if they didn't even know what Mint is.
                            However there was still some scepticism in my head, I refused to be an elitist because I couldn't even understand what was happening in my body. That was until I discovered Arch. The level of trascendence that Arch made me reach was enough to see what Windows and Mac users were: NPCs with no place in the world, more than being pawns in the 3d chess that we, Linux users, are always playing.
                            This coment was written from an interdimensional Gentoo system managed with the mind, so sorry if I get some spelling wrong.
                            """,
                    0
            );
        }
    }

    public FullPostInfoDTO getFullPostInfo(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return null;
        }
        return mapper.map(post, FullPostInfoDTO.class);
    }

    public List<PostFeedDTO> getFeed(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size))
                .map(post -> mapper.map(post, PostFeedDTO.class))
                .toList();
    }

    public Long createPost(PostCreationDTO dto, String username) throws IOException {
        Post post = new Post();
        post.setPostType(dto.getImage().isEmpty() ? PostType.TEXT : PostType.IMAGE);
        post.setComments(new ArrayList<>());
        post.setCommentCount(0);
        post.setContent(dto.getContent());
        post.setTitle(dto.getTitle());
        post.setGroup(groupService.getGroupByName(dto.getGroupName()));
        post.setAuthor(userService.getUserByUsername(username));
        post.setCreated(Instant.now());

        String imageUrl = cloudinaryService.postImage(dto.getImage());

        Post save = postRepository.save(post);
        return save.getId();
    }


    private void createPost(String authorUsername, String groupName, String title, String content, Integer commentCount) {
        Post post = new Post();
        post.setPostType(PostType.TEXT);
        post.setCreated(Instant.now());
        post.setAuthor(userService.getUserByUsername(authorUsername));
        post.setGroup(groupService.getGroupByName(groupName));
        post.setTitle(title);
        post.setContent(content);
        post.setComments(new ArrayList<>());
        post.setCommentCount(commentCount);
        postRepository.save(post);
    }
}