INSERT INTO specialties (code, name)
VALUES (100, 'Стрелковые'),
       (101, 'Пулемётные'),
       (102, 'Гранатометные'),
       (106, 'Войсковой разведки'),
       (107, 'Частей и подразделений спецназа (СпН)'),
       (109, 'Снайперские'),
       (113, 'Танкист'),
       (121, 'БМП'),
       (122, 'БМД'),
       (124, 'БТР'),
       (131, 'Автоматизация во всех видах и родах войск'),
       (134, 'ЗРВ (Зенитно-ракетные войска'),
       (166, 'Инженерные войска'),
       (187, 'Радиационная, химическая и биологическая разведка и дозиметрический контроль'),
       (252, 'Машинист бронепоезда'),
       (262, 'Техник'),
       (422, 'УКВ радиостанции'),
       (423, 'Радиотелефонист'),
       (461, 'Коротковолновые радиостанции'),
       (427, 'Однополюсные радиостанции средней и малой дальности'),
       (553, 'Космическая радиосвязь'),
       (600, 'Оператор ЭВМ'),
       (756, 'Механик технического подразделения'),
       (757, 'Мастер технического подразделения'),
       (760, 'Старший кондуктор'),
       (837, 'Водители транспортных средств категории ВС'),
       (838, 'Автомобили. Эксплуатация автомобильной техники'),
       (878, 'Санитар'),
       (879, 'Медик'),
       (998, 'Пригодный для службы в армии, но её не проходил'),
       (999, 'Ограниченно пригоден к службе в армии, но не имеет военной подготовки')
ON CONFLICT DO NOTHING;