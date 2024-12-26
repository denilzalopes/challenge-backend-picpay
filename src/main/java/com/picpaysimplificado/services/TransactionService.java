package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        // Validate the transaction (balance check, other validations)
        userService.validateTransaction(sender, transaction.value());

        // Check if the transaction is authorized
        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada.");
        }

        // Create the transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // Update user balances
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        // Save the transaction and users
        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        // Send notifications
        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return newTransaction;
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) throws Exception {
        try {
            // Call the external authorization API
            ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

            // Check if the response is OK
            if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
                // Extract the message from the response
                String message = (String) authorizationResponse.getBody().get("message");

                // Log the response message for debugging
                System.out.println("Réponse de l'API d'autorisation: " + message);

                // If the message indicates authorization, return true
                return "Autorizado".equalsIgnoreCase(message);
            } else {
                // Log the error if the status code is not OK
                System.out.println("Erreur de l'API d'autorisation, code: " + authorizationResponse.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            // Log any exception that occurs during the API call
            System.out.println("Exception lors de l'appel à l'API d'autorisation: " + e.getMessage());
            return false;
        }
    }
}
