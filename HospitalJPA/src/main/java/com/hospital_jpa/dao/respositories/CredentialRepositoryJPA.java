package com.hospital_jpa.dao.respositories;

import com.hospital_jpa.dao.interfaces.CredentialRepository;
import com.hospital_jpa.dao.model.Credential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class CredentialRepositoryJPA implements CredentialRepository {

    private final MongoDatabase database;

    public CredentialRepositoryJPA(MongoUtils mongoUtils) {
        this.database = mongoUtils.getDatabase();
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection("credentials");
    }

    @Override
    public List<Credential> getAll() {
        List<Credential> credentials = new ArrayList<>();
        for (Document doc : getCollection().find()) {
            credentials.add(convertDocumentToCredential(doc));
        }
        return credentials;
    }

    @Override
    public boolean delete(String patientId) {
        return getCollection().deleteOne(eq("patient_id", new ObjectId(patientId))).getDeletedCount() > 0;
    }


    @Override
    public Credential get(String username) {
        Document doc = getCollection().find(eq("username", username)).first();
        return doc != null ? convertDocumentToCredential(doc) : null;
    }

    private Document convertCredentialToDocument(Credential credential) {
        if(credential.getPatientId()==null){
            return new Document("_id", new ObjectId(String.valueOf(credential.getId())))
                    .append("username", credential.getUsername())
                    .append("password", credential.getPassword())
                    .append("doctor_id", credential.getDoctorId());

        }else{
            return new Document("_id", new ObjectId(String.valueOf(credential.getId())))
                    .append("username", credential.getUsername())
                    .append("password", credential.getPassword())
                    .append("patient_id", credential.getPatientId());
        }


    }


    private Credential convertDocumentToCredential(Document doc) {
        return new Credential(
                doc.getObjectId("_id").toHexString(),
                doc.getString("username"),
                doc.getString("password"),
                doc.getString("patient_id")
        );
    }
}
