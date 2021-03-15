package es.upo.witzl.proyectotfg.samples.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ElementCollection;
import javax.persistence.Id;
import java.util.Collection;

@Document(collection = "data_values")
public class DataValue {

    @Id
    private String id;

    @ElementCollection
    private Collection<Integer> values;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<Integer> getValues() {
        return values;
    }

    public void setValues(Collection<Integer> values) {
        this.values = values;
    }
}
