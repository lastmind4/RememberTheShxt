package filters

import javax.inject.Inject

import play.api.http.DefaultHttpFilters

class Filters @Inject()(loggingFilter: LoggingFilter, sessionFilter: SessionFilter) extends DefaultHttpFilters(loggingFilter, sessionFilter)