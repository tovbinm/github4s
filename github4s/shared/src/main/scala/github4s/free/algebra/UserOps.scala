/*
 * Copyright 2016-2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s.free.algebra

import github4s.GithubResponses._
import github4s.free.domain.{Pagination, User}

import github4s.GithubResponses.GHResponse
import github4s.{GithubApiUrls, HttpClient, HttpRequestBuilderExtension}
import github4s.free.domain.{Pagination, User}
import io.circe.generic.auto._
import github4s.free.algebra.UserOps
import github4s.Config

import freestyle._

/**
 * Exposes Users operations as a Free monadic algebra that may be combined with other Algebras via
 * Coproduct
 */
object UserOps {
  @free trait UserOpsM {

    def getUser(username: String): FS[GHResponse[User]]

    def getAuthUser: FS[GHResponse[User]]

    def getUsers(since: Int, pagination: Option[Pagination]): FS[GHResponse[List[User]]]

  }

  trait Implicits {
    implicit def UserOpsMHandler[M[_]](implicit M: Monad[M]): UserOpsM.Handler[M] =
      new UserOpsM.Handler[M] {
        val httpClient = new HttpClient[C, M]

        /**
         * Get information for a particular user
         *
         * @param accessToken to identify the authenticated user
         * @param headers     optional user headers to include in the request
         * @param username    of the user to retrieve
         * @return GHResponse[User] User details
         */
        def get(config: Config, username: String): M[GHResponse[User]] =
          httpClient.get[User](config.accessToken, s"users/$username", config.headers)

        /**
         * Get information of the authenticated user
         *
         * @param accessToken to identify the authenticated user
         * @param headers     optional user headers to include in the request
         * @return GHResponse[User] User details
         */
        def getAuth(config: Config): M[GHResponse[User]] =
          httpClient.get[User](config.accessToken, "user", config.headers)

        /**
         * Get users
         *
         * @param accessToken to identify the authenticated user
         * @param headers     optional user headers to include in the request
         * @param since       The integer ID of the last User that you've seen.
         * @param pagination  Limit and Offset for pagination
         * @return GHResponse[List[User] ] List of user's details
         */
        def getUsers(
            config: Config,
            since: Int,
            pagination: Option[Pagination] = None): M[GHResponse[List[User]]] =
          httpClient
            .get[List[User]](
              config.accessToken,
              "users",
              config.headers,
              Map("since" → since.toString),
              pagination)
      }

  }

  object implicits extends Implicits

}
