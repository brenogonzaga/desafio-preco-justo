package br.app.precojusto.models.dto.request;

import br.app.precojusto.models.Cliente;
import br.app.precojusto.models.Endereco;
import br.app.precojusto.models.Telefone;
import br.app.precojusto.models.Telefone.TipoTelefone;
import br.app.precojusto.models.dto.base.BaseClienteDTO;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClienteRequestDTO extends BaseClienteDTO {

    private boolean elegivelDesconto;

    @NotBlank
    private String password;

    private List<EnderecoDTO> enderecos;
    private List<TelefoneDTO> telefones;

    @Data
    public static class EnderecoDTO {

        private String logradouro;
        private String numero;
        private String complemento;
        private String bairro;
        private String cidade;
        private String estado;
        private String cep;
        private String pais;

        public Endereco toEndereco() {
            Endereco endereco = Endereco.builder()
                .logradouro(this.logradouro)
                .numero(this.numero)
                .complemento(this.complemento)
                .bairro(this.bairro)
                .cidade(this.cidade)
                .estado(this.estado)
                .cep(this.cep)
                .pais(this.pais)
                .build();
            return endereco;
        }
    }

    @Data
    public static class TelefoneDTO {

        private TipoTelefone tipo;
        private Integer ddd;
        private String numero;

        public Telefone toTelefone() {
            Telefone telefone = Telefone.builder().tipo(this.tipo).ddd(this.ddd).numero(this.numero).build();
            return telefone;
        }
    }

    public Cliente toCliente() {
        Cliente cliente = Cliente.builder()
            .nome(this.getNome())
            .cpf(this.getCpf())
            .email(this.getEmail())
            .hashedPassword(this.getPassword())
            .elegivelDesconto(this.isElegivelDesconto())
            .enderecos(this.getEnderecos().stream().map(EnderecoDTO::toEndereco).collect(Collectors.toSet()))
            .telefones(this.getTelefones().stream().map(TelefoneDTO::toTelefone).collect(Collectors.toSet()))
            .build();
        return cliente;
    }
}
