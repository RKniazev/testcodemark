package ru.rkniazev.testcodemark.controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;

public class Status {
    private boolean success;
    private ArrayList<String> listErrors;
    private String body;

    public Status(boolean success){
        this.success = success;
        this.listErrors = new ArrayList<String>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void addErrors(String error){
        listErrors.add(error);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(
                "CustomCarSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(Status.class, new StatusCustomSerializer());
        mapper.registerModule(module);
        try {
            result = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    class StatusCustomSerializer extends StdSerializer<Status> {

        public StatusCustomSerializer() {
            this(null);
        }

        public StatusCustomSerializer(Class<Status> t) {
            super(t);
        }

        @Override
        public void serialize(
                Status status, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("success", String.valueOf(status.success));
            if (!status.isSuccess()){
                String[] errors = new String[listErrors.size()];
                status.listErrors.toArray(errors);
                jsonGenerator.writeFieldName("errors");
                jsonGenerator.writeArray(errors,0,errors.length);
            } else if (status.body != null){
                jsonGenerator.writeFieldName("request");
                jsonGenerator.writeRaw(":" + status.body);
            }
            jsonGenerator.writeEndObject();
        }
    }
}


