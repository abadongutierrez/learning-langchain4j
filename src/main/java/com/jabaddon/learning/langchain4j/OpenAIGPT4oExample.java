package com.jabaddon.learning.langchain4j;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.time.Duration;

public class OpenAIGPT4oExample {
    public static void main(String[] args) {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(Duration.ofSeconds(60))
                .modelName("gpt-4o")
                .build();

        UserMessage userMessage = UserMessage.userMessage(
                TextContent.from("Extract the content of this letter"),
                ImageContent.from("https://i0.wp.com/www.themarginalian.org/wp-content/uploads/2012/12/howtowriteletters7.jpg?w=680&ssl=1")
        );
        AiMessage content = chatModel.generate(userMessage).content();
        System.out.println(content.text());

        userMessage = UserMessage.userMessage(
                TextContent.from("Extract the content of this letter"),
                ImageContent.from("https://i.pinimg.com/736x/f7/04/19/f70419a7824bfbd7fc20c10a48cce127.jpg")
        );
        content = chatModel.generate(userMessage).content();
        System.out.println(content.text());
    }

}
