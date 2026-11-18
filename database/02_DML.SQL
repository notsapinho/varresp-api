-- VarreSP | Amostra do CSV SP156 e das previsões de teste do notebook.
-- Sete distritos, quatro semanas completas de dezembro de 2025.
-- Apenas na amostra de previsões foi omitido LIMAO em 22/12 para demonstrar a consulta de cobertura.
-- Execute após 01_DDL.SQL, em tabelas vazias. A carga é confirmada ao final.

INSERT INTO distrito (id_distrito, nome) VALUES (1, 'BELEM');
INSERT INTO distrito (id_distrito, nome) VALUES (2, 'JABAQUARA');
INSERT INTO distrito (id_distrito, nome) VALUES (3, 'LAPA');
INSERT INTO distrito (id_distrito, nome) VALUES (4, 'LIBERDADE');
INSERT INTO distrito (id_distrito, nome) VALUES (5, 'LIMAO');
INSERT INTO distrito (id_distrito, nome) VALUES (6, 'SANTA CECILIA');
INSERT INTO distrito (id_distrito, nome) VALUES (7, 'VILA MARIANA');

INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (1, DATE '2025-12-01', 14);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (1, DATE '2025-12-08', 9);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (1, DATE '2025-12-15', 23);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (1, DATE '2025-12-22', 23);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (2, DATE '2025-12-01', 24);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (2, DATE '2025-12-08', 26);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (2, DATE '2025-12-15', 33);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (2, DATE '2025-12-22', 19);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (3, DATE '2025-12-01', 75);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (3, DATE '2025-12-08', 125);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (3, DATE '2025-12-15', 104);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (3, DATE '2025-12-22', 56);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (4, DATE '2025-12-01', 7);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (4, DATE '2025-12-08', 5);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (4, DATE '2025-12-15', 1);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (4, DATE '2025-12-22', 1);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (5, DATE '2025-12-01', 5);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (5, DATE '2025-12-08', 3);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (5, DATE '2025-12-15', 3);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (5, DATE '2025-12-22', 4);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (6, DATE '2025-12-01', 5);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (6, DATE '2025-12-08', 6);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (6, DATE '2025-12-15', 1);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (6, DATE '2025-12-22', 13);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (7, DATE '2025-12-01', 13);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (7, DATE '2025-12-08', 7);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (7, DATE '2025-12-15', 5);
INSERT INTO demanda_semanal (id_distrito, semana, pedidos) VALUES (7, DATE '2025-12-22', 8);

INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (1, DATE '2025-12-01', 13.680, 0.954269, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (1, DATE '2025-12-08', 13.049, 0.946070, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (1, DATE '2025-12-15', 11.237, 0.904684, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (1, DATE '2025-12-22', 16.013, 0.976165, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (2, DATE '2025-12-01', 24.430, 0.999802, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (2, DATE '2025-12-08', 23.917, 0.999789, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (2, DATE '2025-12-15', 24.658, 0.999837, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (2, DATE '2025-12-22', 27.265, 0.999935, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (3, DATE '2025-12-01', 76.940, 1.000000, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (3, DATE '2025-12-08', 65.985, 1.000000, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (3, DATE '2025-12-15', 83.519, 1.000000, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (3, DATE '2025-12-22', 76.051, 1.000000, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (4, DATE '2025-12-01', 5.235, 0.261933, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (4, DATE '2025-12-08', 6.285, 0.332772, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (4, DATE '2025-12-15', 5.707, 0.306639, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (4, DATE '2025-12-22', 4.335, 0.225596, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (5, DATE '2025-12-01', 4.192, 0.195142, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (5, DATE '2025-12-08', 5.130, 0.236831, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (5, DATE '2025-12-15', 4.473, 0.205084, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (6, DATE '2025-12-01', 16.449, 0.995843, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (6, DATE '2025-12-08', 15.449, 0.993627, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (6, DATE '2025-12-15', 15.543, 0.993271, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (6, DATE '2025-12-22', 13.609, 0.986990, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (7, DATE '2025-12-01', 13.095, 0.957625, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (7, DATE '2025-12-08', 13.295, 0.957114, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (7, DATE '2025-12-15', 11.107, 0.913993, 7);
INSERT INTO previsao_semanal (id_distrito, semana, volume_previsto, prob_alta, limite_alta) VALUES (7, DATE '2025-12-22', 10.113, 0.870618, 7);

COMMIT;
