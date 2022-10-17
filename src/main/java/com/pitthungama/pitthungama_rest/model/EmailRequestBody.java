package com.pitthungama.pitthungama_rest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailRequestBody {

    public String receiverName;
    public String receiverEmail;
    public List<String> participants;
    public String payment;
    public String ticketCode;
}
