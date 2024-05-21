package com.example.desafiofinal.model;

import com.example.desafiofinal.model.Singleton.Nave;
import com.example.desafiofinal.model.Singleton.Tierra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telemetria")
public class Telemetry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "tierra_id", nullable = false)
    private Tierra tierra;

    @ManyToOne
    @JoinColumn(name = "nave_id", nullable = false)
    private Nave nave;
    @Column(name = "datos", nullable = false)
    private String datos;
}
