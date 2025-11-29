package com.jabaddon.learning.langchain4j;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;

public class ToolsLowLevelApiExample {
    public static void main(String[] args) {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .timeout(Duration.ofSeconds(60))
                .logResponses(true)
                .logRequests(true)
                .modelName("gpt-3.5-turbo")
                .build();

        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(new LowLevelTool());

        if (toolSpecifications.isEmpty()) System.out.println("Tools is empty!");
        toolSpecifications.forEach(ts -> {
            System.out.printf("%s, %s, %s", ts.name(), ts.description(), ts.parameters());
        });

        List.of(
                "1 + 10",
                "230302 + 12323123",
                "What is the square root of 9",
                "What is the square root of 9000000",
                "What is the weather in Londong in Celcius",
                "What is the weather in Mexico City in Fahrenheit"
        ).forEach(msg -> {
            UserMessage userMessage = UserMessage.userMessage(TextContent.from(msg));
            ChatRequest request = ChatRequest.builder()
                    .messages(Collections.singletonList(userMessage))
                    .toolSpecifications(toolSpecifications)
                    .build();
            ChatResponse generate = chatModel.chat(request);
            System.out.printf("Response: %s, hasToolExecution: %b", generate.aiMessage().text(), generate.aiMessage().hasToolExecutionRequests());
        });

    }
}

enum TemperatureUnit { CELSIUS, FAHRENHEIT }

record CityWeather(double celsiusTemp, double fahrenheitTemp) {}

class LowLevelTool {
    static final Map<String, CityWeather> map = Map.of("London", new CityWeather(20, 68),
            "Mexico City", new CityWeather(21, 72));

    @Tool("Sums 2 given numbers")
    public double sum(double a, double b) {
        System.out.println("Calling sum...");
        return a + b;
    }

    @Tool("Returns a square root of a given number")
    public double squareRoot(double x) {
        System.out.println("Calling squareRoot...");
        return Math.sqrt(x);
    }

    @Tool("Returns the weather forecast for a given city")
    String getWeather(
            @P("The city for which the weather forecast should be returned") String city,
            TemperatureUnit temperatureUnit
    ) {
        System.out.println("Calling getWeather...");
        CityWeather cityWeather = map.get(city);
        return String.valueOf(switch (temperatureUnit) {
            case CELSIUS -> cityWeather.celsiusTemp();
            case FAHRENHEIT -> cityWeather.fahrenheitTemp();
        });
    }
}
