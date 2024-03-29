package com.infidotte.kafka.controller;


import com.infidotte.kafka.consumer.CustomKafkaConsumer;
import com.infidotte.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final KafkaProducer producer;

    private final CustomKafkaConsumer consumer;

    /*
     * в консоль выводится два сообщения:
     *   Received message: ?
     *   Received message: Async message
     * во всех гайдах, что я прочитал в метод консьюмера
     * передается стринга, но почему она не перезаписывается
     * прочитанным сообщением?
     * Каждый раз когда я вызываю send() продюссера,
     *  метод @KafkaListener listen(String message) вызывался сам
     *      и выводил сообщение продюссера, но получалось так
     *
     * передаю заглушку listen(message = "?") -> внутри метода message
     *  перезаписывается на сообщение из кафки, затем опять перезаписывается
     *      на заглушку "?" и return`ится, в чем смысл? как обрабатывать сообщения?
     *          класть в объект обертку и возвращать его? п.с не работает
     * Received message ? and converted to record
     * Received message Async message and converted to record
     * но в контроллер возвращается "?"
     *
     * Когда я написал код с KafkaConsumer метод аннотированный
     *  @KafkaListener перестал работать, выводит только "?"
     *      управление перешло явно прописанному консьюмеру?
     *
     *
     * */
    @GetMapping("/sendAsync")
    public String sendAsync() {
        producer.sendAsyncMessage("Async message");
        return consumer.read("?").message();
    }

    /*
     * После старта приложения первый запрос по этому урлу
     *   походу подписывает консьюмера
     *
     * Второй раз выводит "Sync message"
     *
     * Написав два листенера, работает только один из них,
     *  если второй явно не вызвать
     * */
    @GetMapping("/sendSync")
    public String sendSync() throws ExecutionException, InterruptedException, TimeoutException {
        AtomicReference<String> message = new AtomicReference<>("");
        producer.sendSyncMessage("Sync message").whenComplete(
                (recordResult, exception) -> {
                    if (exception == null)
                        message.set(consumer.listen());
                }).get(1, TimeUnit.MINUTES);
        return message.get();
    }
}
