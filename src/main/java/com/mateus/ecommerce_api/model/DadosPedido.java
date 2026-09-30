package com.mateus.ecommerce_api.model;

import java.util.List;

public class DadosPedido {
    private List<ItemCarrinhoDto> itens;

    public List<ItemCarrinhoDto> getItens() { return itens; }
    public void setItens(List<ItemCarrinhoDto> itens) { this.itens = itens; }

    public static class ItemCarrinhoDto {
        private Long produtoId;
        private Integer quantidade;

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }
}