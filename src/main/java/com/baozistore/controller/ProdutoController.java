package com.baozistore.controller;

import com.baozistore.model.Produto;
import com.baozistore.repository.ProdutoRepository;
import com.baozistore.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;
    private final PedidoRepository pedidoRepository;

    public ProdutoController(ProdutoRepository repository, PedidoRepository pedidoRepository) {
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto obj) {
        obj.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(obj));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Produto> listar() {
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (pedidoRepository.existsByProdutoId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não é possível apagar: o produto possui pedidos");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
