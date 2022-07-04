package com.example.grouperapi.service;

import com.example.grouperapi.model.dto.AddCommentDTO;
import com.example.grouperapi.model.dto.ResponseType;
import com.example.grouperapi.model.entities.*;
import com.example.grouperapi.repositories.CommentRepository;
import com.example.grouperapi.repositories.ImageRepository;
import com.example.grouperapi.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;


    @Transactional
    public void addComment(AddCommentDTO addCommentDTO, String username) throws IOException {
        if (addCommentDTO.getResponseType() == ResponseType.POST) {
            PostComment postComment = new PostComment();
            User author = userService.getUserByUsername(username);
            postComment.setAuthor(author);
            postComment.setContents(addCommentDTO.getContent());
            if (!addCommentDTO.getImage().isEmpty()) {
                Image image = cloudinaryService.postImage(addCommentDTO.getImage());
                postComment.setImage(image);
                imageRepository.save(image);
            }
            postComment.setCreated(Instant.now());
            Post post = postRepository.findById(addCommentDTO.getId()).orElseThrow(() -> new RuntimeException("No post with id in the database"));
            commentRepository.save(postComment);
            postComment.setPost(post);
            post.setCommentCount(post.getCommentCount() + 1);
            postRepository.save(post);
        } else if (addCommentDTO.getResponseType() == ResponseType.COMMENT) {
            Reply reply = new Reply();
            User author = userService.getUserByUsername(username);
            reply.setAuthor(author);
            reply.setContents(addCommentDTO.getContent());
            if (!addCommentDTO.getImage().isEmpty()) {
                Image image = cloudinaryService.postImage(addCommentDTO.getImage());
                reply.setImage(image);
                imageRepository.save(image);
            }
            reply.setCreated(Instant.now());
            Comment comment = commentRepository.findById(addCommentDTO.getId()).orElseThrow(() -> new RuntimeException("invalid comment id"));
            comment.getReplies().add(reply);
            commentRepository.save(reply);
            commentRepository.save(comment);
        }
    }

    public void seedComments() {
        if (commentRepository.count() == 0) {
            commentsLinux();
        }
    }

    private void commentsLinux() {
        postComment1();
        postComment2();
    }

    private void postComment2() {
        PostComment topLevelReply = createTopComment(1L, "user6", """
                cat will normally copy a chunk at the time, to ensure it works with everything that claims to be a file (which in POSIX is just about everything).
                There are some optimizations possible. For example, a file which is what we typically refer to as a file (say, a text file stored on a disk) can be memory mapped, and sy shown by piping the memory to stdout. But that will break on some things, and is therefore not advisable to use in the generic case of cat, which has to work with everything.
                This is also why grep, for example, is not perfectly optimized, but works in a rather naive manner when it comes to files and pipes. It has to work with everything. Something like ripgrep, which has a much narrower design (only work on files which can be memory mapped) will have incredibly much better performance - but will break when used to solve general problems.
                You will find this is typical of the core utilities. They are designed to be reliable, and work even in strange edge cases. Performance is nice, but not nearly as important as versatility.
                                """);
        List<Reply> replies1 = new ArrayList<>();
        Reply reply1 = createReply("user8", """
                ripgrep author here.
                Something like ripgrep, which has a much narrower design (only work on files which can be memory mapped) will have incredibly much better performance - but will break when used to solve general problems.
                That's incorrect. ripgrep does not have a narrower design than grep and it works just fine on things that can't be memory mapped. You're probably thinking about the silver searcher, which does actually silently fail when it can't memory map files:

                $ grep -En 'core id\s*:\s*0' /proc/cpuinfo
                12:core id              : 0
                174:core id             : 0
                $ ag 'core id\s*:\s*0' /proc/cpuinfo
                $ rg 'core id\s*:\s*0' /proc/cpuinfo
                12:core id              : 0
                174:core id             : 0

                ripgrep only memory maps files in certain situations. Obviously, only when it works. But also typically when it's searching just a few files. If it's crawling a directory, then memory mapping tends to be slower and it falls back to standard read calls, just like grep. So I think you have a number of incorrect assumptions here. :) In any case, memory mapping is a cheap trick. It helps a little bit in some circumstances. ripgrep is fast for a number of other reasons, some of which aren't so interesting (parallelism) and some that are (algorithms, SIMD).
                This is also why grep, for example, is not perfectly optimized, but works in a rather naive manner when it comes to files and pipes. It has to work with everything.
                Can you say more? I suspect there are some misunderstandings here. I would, for example, not call GNU grep's handling of files/pipes "naive." :-)
                """);
        List<Reply> replies2 = new ArrayList<>();
        Reply reply2 = createReply("user6", """
                Why hello, and thanks for ripgrep! I use it a lot, but mainly to quickly search through huge stacks of documentation. And the reason I do that is, I read one of your early articles on its design, and how you used memory mapping, and built myself an internal model of how it works. As I recall, it was in comparison with silver searcher, which I have never been particularly fond of for various reasons.
                I am very happy to learn I was mistaken in that model. :)
                I'll need to give ripgrep a go for my usual grep tasks then. I'll be honest and say it never occurred to me to do so.
                All I mean by naive is that it's akin to cat, and not highly optimized for when there are more efficient ways of accessing media and such. That doesn't necessarily mean it's se, as it has to handle a lot of corner cases.
                """);
        List<Reply> replies3 = new ArrayList<>();
        Reply reply3 = createReply("user7", """
                >I'll need to give ripgrep a go for my usual grep tasks then. I'll be honest and say it never occurred to me to do so.
                Aye, yeah, and if you run into issues please report them. I specifically built ripgrep so that it would work like grep when used in pipelines for example. ag does have a lot of trouble there which motivated me to specifically pay attention to that stuff.
                >All I mean by naive is that it's akin to cat, and not highly optimized for when there are more efficient ways of accessing media and such. That doesn't necessarily mean it's se, as it has to handle a lot of corner cases.
                To be clear, I don't think there's anything GNU grep can do that is faster. cat is a different program. It doesn't have to search the bytes where as grep does, so grep can't use any clever kernel specific APIs to shove bytes from one file descriptor to the other. In that sense, I wouldn't call what GNU grep does "naive," because to me, "naive" tends to y that there is a better way. But there really isn't.
                Well, technically, GNU grep could use memory maps like ripgrep does in certain cases. But like I said, it's a cheap trick. And it does come with downsides. For example, if a file that you memory map is truncated while ripgrep is searching it, then ripgrep aborts with SIGBUS. GNU grep used to use memory maps many moons ago, but they removed support for them because of the SIGBUS issue (I believe) and because they weren't carrying their weight. But other than memory maps, for the task of searching, read is pretty much the best you can do.
                """);
        replies3.add(reply3);
        reply2.setReplies(replies3);
        replies2.add(reply2);

        reply1.setReplies(replies2);
        replies1.add(reply1);
        topLevelReply.setReplies(replies1);
        commentRepository.save(topLevelReply);
    }

    @Transactional
    void postComment1() {
        PostComment topLevelReply1 = createTopComment(1L, "user2", "It depends on the exact ementation, but most of them either use a fixed size buffer or a syscall to have the kernel transfer data directly from the input to the output.");
        List<Reply> replies = new ArrayList<>();
        Reply reply1 = createReply("user4", """
                oh interesting! I didn't even think about the kernel just passing the data from stdin to stdout directly (still a lot to learn about the actual nuts and bolts of linux). Thanks!
                If you had any recommendations for readings to learn more, i'd appreciate it!
                """);
        List<Reply> replies2 = new ArrayList<>();
        replies2.add(
                createReply("user5", """
                        I guess the source code is a nice place to start, if you know C
                        Btw, these fundamental coreutils have been around for decades, so you can be sure they're well optimized
                        """)
        );
        replies2.add(
                createReply("user3", """
                        https://man7.org/linux/man-pages/man2/sendfile.2.html
                        """)
        );
        Reply reply2 = createReply("user6", """
                If the filesystem supports it (btrfs, XFS), cat will actually copy no data whatsoever and leave it to the kernel to add a reflink instead.
                """);
        List<Reply> replies3 = new ArrayList<>();
        Reply reply4 = createReply("user7", """
                Wouldn't that only work if you redirect stdout to a file on the same filesystem as the input
                                """);
        List<Reply> replies5 = new ArrayList<>();
        Reply reply5 = createReply("user6", """
                Yes, of course. Though you still get the reduced overhead of not needing to go through userspace.
                """);
        replies3.add(reply4);
        replies5.add(reply5);
        reply4.setReplies(replies5);
        reply2.setReplies(replies3);
        reply1.setReplies(replies2);
        reply1.getReplies().add(reply2);
        replies.add(reply1);
        topLevelReply1.setReplies(replies);
        commentRepository.save(topLevelReply1);
    }

    private PostComment createTopComment(Long postId, String username, String contents) {
        PostComment postComment = new PostComment();
        postComment.setPost(postRepository.getById(postId));
        postComment.setAuthor(userService.getUserByUsername(username));
        postComment.setContents(contents);
        postComment.setCreated(Instant.now());
        return postComment;
    }

    private Reply createReply(String username, String contents) {
        Reply reply = new Reply();
        reply.setAuthor(userService.getUserByUsername(username));
        reply.setContents(contents);
        reply.setCreated(Instant.now());
        return reply;
    }
}