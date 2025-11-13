package aeds3;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Buscar {
    private final ListaInvertida iv;
    public static final Set<String> STOP_WORDS = new HashSet<>(Set.of(
        "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "?",
        "a", "à", "agora", "ainda", "além", "algmas", "alguns", "ali", "ambos", "antes", "apenas",
        "aquela", "aquelas", "aquele", "aqueles", "aqui", "as", "assim", "até", "atrás", "através", "aí",
        "bem", "bom", "bastante", "baixo",
        "cada", "cá", "coisa", "com", "como", "contra", "contudo", "cuja", "cujas", "cujo", "cujos",
        "da", "das", "de", "dela", "dele", "deles", "demais", "depois", "desde", "dessa", "desse", "desta", "deste",
        "disto", "disso", "daquele", "daquela", "daqueles", "daquelas",
        "do", "dos",
        "e", "é", "ela", "elas", "ele", "eles", "em", "enquanto", "entre", "era", "eram", "essa", "essas", "esse",
        "esses", "esta", "está", "estão", "estas", "estava", "estavam", "este", "estes", "eu", "era", "éramos", "eram",
        "foi", "foram", "fui", "fomos", "fora",
        "há", "havia", "haviam",
        "isso", "isto", "isso", "inclusive", "igualmente",
        "já", "jamais",
        "lá", "lhe", "lhes", "logo",
        "mais", "mas", "mesma", "mesmas", "mesmo", "mesmos", "meu", "meus", "minha", "minhas", "muito",
        "muitos", "menos", "mesmo", "mesmos", "muito", "muita", "muitas",
        "na", "nas", "nem", "no", "nos", "nós", "nossa", "nossas", "nosso", "nossos", "num", "numa", "nunca",
        "ninguém", "nada", "nenhum", "nenhuma",
        "o", "os", "ou", "onde", "outra", "outro", "outras", "outros",
        "para", "pela", "pelas", "pelo", "pelos", "pode", "podem", "podia", "podiam", "pois", "por", "porque",
        "porém", "portanto", "próprio", "própria", "pouco", "primeiro",
        "qual", "quais", "quando", "quanto", "que", "quem",
        "se", "sem", "seu", "seus", "só", "sob", "sobre", "sua", "suas", "sempre", "senão", "sendo",
        "tal", "também", "tem", "têm", "tenho", "ter", "teu", "teus", "toda", "todas", "todo", "todos",
        "tu", "tua", "tuas", "tudo", "tão", "talvez",
        "um", "uma", "umas", "uns", "uns", "umas",
        "vai", "vão", "vou", "vamos", "vão", "vindo", "vinha",
        "você", "vocês", "vos", "vosso", "vossos", "vossa", "vossas",
        "agora", "então", "assim", "aliás", "ainda", "pois", "portanto", "contudo", "todavia", "entretanto", "apesar", "depois", "antes",
        "pra", "pro", "tava", "tô", "tava", "tive", "tiveram", "tivemos",
        "era", "será", "seriam", "seria", "serei", "seremos"
    ));

    public Buscar(final ListaInvertida iv) {
        this.iv = iv;
    }

    public static String removeAccents(final String texto) {
        if (texto == null)
            return null;
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

    public List<String> removeStopWords(final String texto) {
        List<String> s = Arrays.asList(removeAccents(texto).toLowerCase().split(" "));

        return s.stream().filter(v -> !Objects.isNull(v) && !STOP_WORDS.contains(v)).toList();
    }

    public void delete(final String texto, final int id) {
        List<String> s = removeStopWords(texto);
        s.forEach(v -> {
            try {
                this.iv.delete(v, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try { this.iv.decrementaEntidades(); } 
        catch (Exception e) { e.printStackTrace(); }
    }

    public void create(final String texto, final int id) {
        List<String> s = removeStopWords(texto);
        HashMap<String, Integer> m = new HashMap<>();

        s.forEach(v -> m.put(v, m.getOrDefault(v, 0) + 1));
        m.forEach((k, v) -> {
            try {
                this.iv.create(k, new ElementoLista(id, v / s.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try { this.iv.incrementaEntidades(); } 
        catch (Exception e) { e.printStackTrace(); }
    }

    public List<Integer> search(final String texto) throws Exception {
        List<String> s = removeStopWords(texto);
        int total = this.iv.numeroEntidades();

        HashMap<Integer, Float> m = new HashMap<>();
        s.forEach(v -> {
            List<ElementoLista> r = List.of();
            try {
                r = Arrays.asList(this.iv.read(v));

                if (r.size() > 0) {
                    float idf = (float) Math.log(total / r.size());
                    r.forEach(e -> {
                        float newWeight = m.getOrDefault(e.getId(), 0f) + (e.getFrequencia() * idf);
                        m.put(e.getId(), newWeight);
                    });
                }
            } catch (Exception e) { e.printStackTrace(); }
        });

        List<Integer> r = new ArrayList<>(m.keySet());
        r.sort((a, b) -> Float.compare(m.get(b), m.get(a)));
        return r;
    }
}
