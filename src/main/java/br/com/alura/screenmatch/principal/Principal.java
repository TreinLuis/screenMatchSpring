package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private int temporadas;
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private Scanner in = new Scanner(System.in);
    private final String API_KEY = "&apikey=47794d59";

    public void exibeMenu() throws JsonProcessingException {
        ConverteDados conversor = new ConverteDados();

        System.out.println("Digite a série a ser pesquisada: ");
        String serie = in.nextLine().replace(" ", "+").toLowerCase();
        String uri = ENDERECO + serie + API_KEY;
        var json = consumoApi.obterDados(uri);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        System.out.println("Deseja adicionar temporada á pesquisa?");
        var decidir = in.nextLine();


        if (decidir.equalsIgnoreCase("sim")) {
            System.out.println("Deseja pesquisar todas as temporada?");
            decidir = in.nextLine();

            if (decidir.equalsIgnoreCase("sim")) {
                List<DadosTemporada> temp = new ArrayList<>();
                for (int i = 1; i <= dados.totalTemporadas(); i++) {
                    temporadas = 1;
                    uri = ENDERECO + serie + "&season=" + temporadas + API_KEY;
                    json = consumoApi.obterDados(uri);
                    DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                    System.out.println(dadosTemporada);
                    temp.add(dadosTemporada);
                }
                temp.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));//Printando somente os episodeos de todas as temporadas(isto é um lambda)
            }
            System.out.println("Digite a temporada: ");
            temporadas = in.nextInt();
            in.nextLine();
            uri = ENDERECO + serie + "&season=" + temporadas + API_KEY;
            json = consumoApi.obterDados(uri);

            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            System.out.println("Deseja adicionar um unico episodio á pesquisa?");
            decidir = in.nextLine();
            if (decidir.equalsIgnoreCase("sim")) {
                System.out.println("Digite o episodio: ");
                int episodio = in.nextInt();
                uri = ENDERECO + serie + "&season=" + temporadas + "&episode=" + episodio + API_KEY;
                json = consumoApi.obterDados(uri);

                DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
                System.out.println(dadosEpisodio);
            }
            //System.out.println(dadosTemporada);
        }
        System.out.println(dados);
    }

}
