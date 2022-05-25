package com.example.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@Entity
@XmlRootElement

public class Event {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    // 1-Inicio e Fim de Jogo
    // 2-Golo
    // 3-Cartao Amarelo
    // 4-Cartao Vermelho
    // 5-Jogo interrompido
    // 6-Jogo resumido
    private int type;
    private String title;

    private String info;

    private Date time;

    @ManyToOne(cascade = CascadeType.ALL)
    private Match match;
    
    // redundancia para facilitar a pesquisa
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team;

    @ManyToOne(cascade = CascadeType.ALL)
    private Player player;


    @ManyToOne(cascade = CascadeType.ALL)
    private WebUser userEvent;

    
    //verificacao
    @ManyToOne(cascade = CascadeType.ALL)
    private WebUser userApprovel;

    private boolean aproved; 
   
  
    public Event() {
    }
    
    public Event(String info) {
        this.info = info;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    //@XmlElementWrapper(info = "teamessors")
    //@XmlElement(info = "team")
    

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public String toString() {
        return this.info + "(id = " + this.id + ")";
    }
}
