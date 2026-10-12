package com.baozistore.controller;

import com.baozistore.model.Cliente;
import com.baozistore.repository.ClienteRepository;
import com.baozistore.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;
    private final PedidoRepository pedidoRepository;

    public ClienteController(ClienteRepository repository, PedidoRepository pedidoRepository) {
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente obj) {
        obj.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(obj));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Cliente> listar() {
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (pedidoRepository.existsByClienteId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não é possível apagar: o cliente possui pedidos");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
