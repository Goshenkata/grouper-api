package com.example.grouperapi.config;

import java.util.Map;

import com.cloudinary.Cloudinary;
import com.example.grouperapi.converters.PostCommentsListToPostCount;
import com.example.grouperapi.model.dto.*;
import com.example.grouperapi.model.entities.Post;
import com.example.grouperapi.model.entities.UserEntity;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.AllArgsConstructor;
import ua_parser.Parser;

@Configuration
@AllArgsConstructor
public class ApplicationConfiguration {
    private CloudinaryConfig cloudinaryConfig;

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(Post.class, PostFeedDTO.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        using(new PostCommentsListToPostCount()).map(source.getComments(),
                                destination.getCommentCount());
                    }
                });
        mapper.typeMap(Post.class, FullPostInfoDTO.class)
                .addMappings(new PropertyMap<>() {
                    @Override
                    protected void configure() {
                        using(new PostCommentsListToPostCount()).map(source.getComments(),
                                destination.getCommentCount());
                    }
                });
        mapper.typeMap(UserEntity.class, ObjectSearchReturnDTO.class)
                .addMapping(source -> source.getPfp().getUrl(), ObjectSearchReturnDTO::setImageUrl)
                .addMapping(UserEntity::getUsername, ObjectSearchReturnDTO::setName);

        mapper.typeMap(UserEntity.class, UserInfoDTO.class)
                .addMapping(source -> source.getPfp().getUrl(), UserInfoDTO::setImageUrl)
                .addMapping(UserEntity::getUsername, UserInfoDTO::setName)
                .addMapping(UserEntity::getDescription, UserInfoDTO::setDescription);
        return mapper;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Cloudinary cloudinary() {
        return new Cloudinary(
                Map.of(
                        "cloud_name", cloudinaryConfig.getCloudName(),
                        "api_key", cloudinaryConfig.getApiKey(),
                        "api_secret", cloudinaryConfig.getApiSecret()));
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Grouper API")
                        .description(
                                "Spring backend for Grouper, a social media centered around groups and the Grouper fish ")
                        .version("dev")
                        .license(new License().name("GNU Affero General Public License v3.0")
                                .url("https://www.gnu.org/licenses/agpl-3.0.en.html")));
    }


    @Bean
    public Parser uaParser() {
        return new Parser();
    }


}
