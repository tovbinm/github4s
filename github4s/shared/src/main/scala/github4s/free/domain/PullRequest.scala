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

package github4s.free.domain

case class PullRequest(
    id: Int,
    number: Int,
    state: String,
    title: String,
    body: String,
    locked: Boolean,
    html_url: String,
    created_at: String,
    updated_at: Option[String],
    closed_at: Option[String],
    merged_at: Option[String],
    base: Option[PullRequestBase],
    user: Option[User],
    assignee: Option[User])

case class PullRequestBase(
    label: Option[String],
    ref: String,
    sha: String,
    user: User,
    repo: Repository)

sealed abstract class PRFilter(val name: String, val value: String)
    extends Product
    with Serializable {
  def tupled: (String, String) = name -> value
}

sealed abstract class PRFilterState(override val value: String) extends PRFilter("state", value)
case object PRFilterOpen                                        extends PRFilterState("open")
case object PRFilterClosed                                      extends PRFilterState("closed")
case object PRFilterAll                                         extends PRFilterState("all")

case class PRFilterHead(override val value: String) extends PRFilter("head", value)

case class PRFilterBase(override val value: String) extends PRFilter("base", value)

sealed abstract class PRFilterSort(override val value: String) extends PRFilter("sort", value)
case object PRFilterSortCreated                                    extends PRFilterSort("created")
case object PRFilterSortUpdated                                    extends PRFilterSort("updated")
case object PRFilterSortPopularity                                 extends PRFilterSort("popularity")
case object PRFilterSortLongRunning                                extends PRFilterSort("long-running")

sealed abstract class PRFilterDirection(override val value: String)
    extends PRFilter("direction", value)
case object PRFilterOrderAsc  extends PRFilterDirection("asc")
case object PRFilterOrderDesc extends PRFilterDirection("desc")