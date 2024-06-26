package com.example.desafiofinal.model.Singleton;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nave")
public class Nave
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


}
