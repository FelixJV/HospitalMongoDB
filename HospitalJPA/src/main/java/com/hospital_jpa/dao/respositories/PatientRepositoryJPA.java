package com.hospital_jpa.dao.respositories;

import com.hospital_jpa.dao.model.Patient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class PatientRepositoryJPA implements com.hospital_jpa.dao.interfaces.PatientRepository {
    private final MongoDatabase database;

    public PatientRepositoryJPA(MongoUtils mongoUtils) {
        this.database = mongoUtils.getDatabase();
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection("patients");
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        for (Document doc : getCollection().find()) {
            patients.add(convertDocumentToPatient(doc));
        }
        return patients;
    }

    @Override
    public String save(Patient patient) {
        Document doc = convertPatientToDocument(patient);
        getCollection().insertOne(doc);
        return doc.getObjectId("_id").toHexString();
    }

    @Override
    public void update(Patient patient) {
        getCollection().updateOne(eq("_id", new ObjectId(patient.getId())),
                new Document("$set", convertPatientToDocument(patient)));
    }

    @Override
    public void delete(String patientId) {
        getCollection().deleteOne(eq("_id", new ObjectId(patientId)));
    }

    // Conversión de `Patient` a `Document`
    private Document convertPatientToDocument(Patient patient) {
        return new Document("_id", new ObjectId(patient.getId()))
                .append("name", patient.getName())
                .append("date_of_birth", patient.getBirthDate())
                .append("phone", patient.getPhone());
    }

    // Conversión de `Document` a `Patient`
    private Patient convertDocumentToPatient(Document doc) {
        return new Patient(
                doc.getObjectId("_id").toHexString(),
                doc.getString("name"),
                String.valueOf(doc.getDate("date_of_birth")),
                doc.getString("phone")
        );
    }
}
