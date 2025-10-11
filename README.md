# ExploreWithMe

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

Бэкенд-приложение сервиса афишы. Оно позволит пользователям делиться информацией об интересных событиях и находить
компанию для участия в них.

https://github.com/ds-skoropad/java-explore-with-me/pull/3

## REST
### `Комментарии`

| Доступ    | Метод    | Путь                                        | Описание                     |
| --------- | :------- | :------------------------------------------ | :--------------------------- |
| `public`  | `GET`    | `/comments/{commentId}`                     | Получить комментарий по `Id` |
| `public`  | `GET`    | `/comments/events/{eventId}`                | Получить комментарии события |
| `admin`   | `PATCH`  | `/admin/comments/{commentId}`               | Обновить комментарий по `id` |
| `admin`   | `DELETE` | `/admin/comments/{commentId}`               | Удалить комментарий по `id`  |
| `private` | `GET`    | `/users/{userId}/comments/events/{eventId}` | Получить комментарии события |
| `private` | `POST`   | `/users/{userId}/comments/events/{eventId}` | Создать комментарий          |
| `private` | `PATCH`  | `/users/{userId}/comments/{commentId}`      | Обновить комментарий по `id` |
| `private` | `DELETE` | `/users/{userId}/comments/{commentId}`      | Удалить комментарий по `id`  |