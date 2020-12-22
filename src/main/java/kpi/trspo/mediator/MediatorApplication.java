package kpi.trspo.mediator;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import kpi.trspo.mediator.services.GrpcServices.CargoTypeServiceGrpcImpl;
import kpi.trspo.mediator.services.GrpcServices.MachineryTypeServiceGrpcImpl;
import kpi.trspo.mediator.services.GrpcServices.PersonRoleServiceGrpcImpl;
import kpi.trspo.mediator.services.interfaces.PersonRoleService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import java.io.IOException;

@SpringBootApplication
public class MediatorApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(MediatorApplication.class, args);
        Server personRoleServer = ServerBuilder.forPort(8081)
                .addService(new PersonRoleServiceGrpcImpl())
                .addService(new CargoTypeServiceGrpcImpl())
                .addService(new MachineryTypeServiceGrpcImpl())
                .build();
        personRoleServer.start();
        personRoleServer.awaitTermination();

    }

}
