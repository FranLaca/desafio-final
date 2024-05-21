package com.example.desafiofinal.model.Singleton;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tierra")
public class Tierra
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

}
